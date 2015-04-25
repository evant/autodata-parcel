package me.tatarka.autodata.compiler.plugins;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import me.tatarka.autodata.compiler.AutoDataProcessor;
import me.tatarka.autodata.compiler.model.AutoDataClass;
import me.tatarka.autodata.compiler.model.AutoDataClassBuilder;
import me.tatarka.autodata.compiler.model.AutoDataField;
import me.tatarka.autodata.plugins.AutoParcel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Iterator;

/**
 * Created by evan on 4/25/15.
 */
@AutoService(AutoDataProcessor.class)
public class AutoParcelProcessor implements AutoDataProcessor<AutoParcel> {
    @Override
    public void init(ProcessingEnvironment processingEnvironment) {

    }

    @Override
    public void process(AutoParcel autoParcel, AutoDataClass autoDataClass, AutoDataClassBuilder autoDataClassBuilder) {
        if (!isParcelable(autoDataClass.getElement())) {
            return;
        }

        TypeName genClassType = ClassName.bestGuess(autoDataClass.getGenClassName());
        boolean hasFields = !autoDataClass.getFields().isEmpty();

        // CREATOR
        TypeName creatorType = ParameterizedTypeName.get(ClassName.get(Parcelable.Creator.class), genClassType);
        TypeSpec creator = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(creatorType)
                .addMethod(MethodSpec.methodBuilder("createFromParcel")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.get(Parcel.class), "in")
                        .returns(genClassType)
                        .addStatement("return new $T($L)", genClassType, hasFields ? "in" : "")
                        .build())
                .addMethod(MethodSpec.methodBuilder("newArray")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.INT, "size")
                        .returns(ArrayTypeName.of(genClassType))
                        .addStatement("return new $T[size]", genClassType)
                        .build())
                .build();

        autoDataClassBuilder.addField(FieldSpec.builder(creatorType, "CREATOR", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", creator)
                .build());

        // Parcel in constructor
        if (hasFields) {
            MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addParameter(TypeName.get(Parcel.class), "in");

            CodeBlock.Builder block = CodeBlock.builder().add("this(");
            for (Iterator<AutoDataField> iterator = autoDataClass.getFields().iterator(); iterator.hasNext(); ) {
                AutoDataField field = iterator.next();
                block.add("($T) in.readValue($T.class.getClassLoader())", field.getType(), genClassType);
                if (iterator.hasNext()) {
                    block.add(", ");
                }
            }
            block.add(");\n");
            constructor.addCode(block.build());
            autoDataClassBuilder.addMethod(constructor.build());
        }

        // writeToParcel
        {
            MethodSpec.Builder method = MethodSpec.methodBuilder("writeToParcel")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(Parcel.class), "dest")
                    .addParameter(TypeName.INT, "flags");

            for (AutoDataField field : autoDataClass.getFields()) {
                method.addStatement("dest.writeValue(this.$L)", field.getName());
            }
            autoDataClassBuilder.addMethod(method.build());
        }

        // describeContents
        {
            MethodSpec method = MethodSpec.methodBuilder("describeContents")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.INT)
                    .addStatement("return 0")
                    .build();
            autoDataClassBuilder.addMethod(method);
        }
    }

    private static boolean isParcelable(TypeElement element) {
        for (TypeMirror iface : element.getInterfaces()) {
            if (iface.toString().equals(Parcelable.class.getName())) {
                return true;
            }
        }
        return false;
    }
}
