package com.base.compiler.processor;
import com.base.annotation.Route;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
/**
 * <li>Package: com.base.compiler.processor</li>
 * <li>Author: lihaoran</li>
 * <li>Date:  8/11/21</li>
 * <li>Description: </li>
 */
@SupportedAnnotationTypes({"com.base.basejavapro.annotation.Route","com.base.annotation.Route"})
public class RouteProcessor extends AbstractProcessor {

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "=============RouteProcessor===================："+set.size());

        Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(Route.class);
        for (Element element : routeElements) {

            Route route = element.getAnnotation(Route.class);
            String path = route.path();
            String group = route.group();
            String newClassName = path + "$$Hx";

            StringBuilder builder = new StringBuilder()
                    .append("package com.hongx.processor.auto;\n\n")
                    .append("public class ")
                    .append(newClassName)
                    .append(" {\n\n") // open class
                    .append("\tpublic String getMessage() {\n") // open method
                    .append("\t\treturn \"");
            builder.append(path).append(group).append(" !\\n");
            builder.append("\";\n") // end return
                    .append("\t}\n") // close method
                    .append("}\n"); // close class
            try {
                JavaFileObject source = mFiler.createSourceFile("com.hongx.processor.auto."+newClassName);
                Writer writer = source.openWriter();
                writer.write(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {

            }

            if (!set.isEmpty()) {
                createRoute(mFiler,newClassName,path);
                createTest();
                createInfe();
                createDataBean();
            }

        }



        return false;
    }

    public static void createRoute(Filer filer,String className,String path) {
        //使用 MethodSpec 主方法main生成
        MethodSpec main = MethodSpec.methodBuilder("getMessage")      //主方法的名称
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S",path)
                .build();
        //	使用 TypeSpec 生成 HelloWorld 类
        TypeSpec typeSpec = TypeSpec.classBuilder(className)   //主类的名称
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .addJavadoc("注释注解代码块")
                .build();
        try {
            JavaFile javaFile = JavaFile.builder("com.javapoet.processor.auto", typeSpec)
                    .build();
            /**
             * 代码写入控制台
             */
            javaFile.writeTo(System.out);
            /**
             * 代码写入文件 E:\\FastEc\\latte_compiler\\src\\main\\java
             */
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTest() {
        //使用 MethodSpec 主方法main生成
        MethodSpec main = MethodSpec.methodBuilder("main")      //主方法的名称
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();
        //	使用 TypeSpec 生成 HelloWorld 类
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")   //主类的名称
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .addJavadoc("注释注解代码块")
                .build();
        try {
            JavaFile javaFile = JavaFile.builder("com.zbc.latte_compiler.javapoeatdemo", helloWorld)
                    .build();
            /**
             * 代码写入控制台
             */
            javaFile.writeTo(System.out);
            /**
             * 代码写入文件 E:\\FastEc\\latte_compiler\\src\\main\\java
             */
            File file = new File("latte_compiler\\src\\main\\java");
            System.out.println("___" + file.getAbsolutePath());
            javaFile.writeTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *     package com.zbc.latte_compiler.javapoeatdemo;
     *     import java.lang.Object;
     *     import java.lang.String;
     *     interface BaseCallback {
     *         String SUCCESS = "msgSuccess";
     *
     *         String FAIL = "msgFail";
     *
     *         void onStart();
     *
     *         void onFinish();
     *
     *         void onError(int errorCode, String msg);
     *
     *         void onSuccess(Object o);
     *     }
      */
    private static void createInfe() {
        TypeSpec BaseCallback = TypeSpec.interfaceBuilder("BaseCallback")
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(String.class, "SUCCESS")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", "msgSuccess")
                        .build())
                .addField(FieldSpec.builder(String.class, "FAIL")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", "msgFail")
                        .build())
                .addMethod(MethodSpec.methodBuilder("onStart")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .build())
                .addMethod(MethodSpec.methodBuilder("onFinish")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .build())
                .addMethod(MethodSpec.methodBuilder("onError")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(int.class, "errorCode")
                        .addParameter(String.class, "msg")
                        .build())
                .addMethod(MethodSpec.methodBuilder("onSuccess")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(Object.class, "errorCode")
                        .build())
                .build();

        try {
            JavaFile javaFile = JavaFile.builder("com.zbc.latte_compiler.javapoeatdemo", BaseCallback)
                    .build();
            /**
             * 代码写入控制台
             */
            javaFile.writeTo(System.out);
            /**
             * 代码写入文件 E:\\FastEc\\latte_compiler\\src\\main\\java
             */
            File file = new File("latte_compiler\\src\\main\\java");
            System.out.println("___" + file.getAbsolutePath());
            javaFile.writeTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * class BaseEntity {
     * 	  private int code;
     *
     * 	  private String msg;
     *
     * 	  private DataBean data;
     *
     * 	  private int getCode() {
     * 	    return this.code;
     *            }
     *
     * 	  private void setCode(int code) {
     * 	    this.code = code;
     *      }
     *
     * 	  private DataBean getData() {
     * 	    return this.data;
     *      }
     *
     * 	  private void setData(DataBean data) {
     * 	    this.data = data;
     *      }
     * 	  static class DataBean {
     * 	    private String name;
     *
             * 	    private String getName() {
     * 	      return this.name;
     * 	    }
     *
             * 	    private void setName(String name) {
     * 	      this.name = name;
     * 	    }
     * 	  }
     * 	}
     */
    private static void createDataBean(){
        String tacosPackage = "com.zbc.latte_compiler.javapoeatdemo";
        ClassName data = ClassName.get(tacosPackage, "BaseEntity", "DataBean");
        /**
         * 1.成员变量生成
         */
        FieldSpec code = FieldSpec.builder(int.class, "code")
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec msg = FieldSpec.builder(String.class, "msg")
                .addModifiers(Modifier.PRIVATE)
                .build();


        //使用 MethodSpec 方法生成
        MethodSpec getCode = MethodSpec.methodBuilder("getCode")      //主方法的名称
                .addModifiers(Modifier.PRIVATE)
                .returns(int.class)
                .addStatement("return this.code")
                .build();


        MethodSpec setCode = MethodSpec.methodBuilder("setCode")      //主方法的名称
                .addModifiers(Modifier.PRIVATE)
                .returns(Void.class)
                .addParameter(int.class, "code")
                .addStatement("this.$N = $N", "code", "code")
                .build();


        //内部类
        FieldSpec name = FieldSpec.builder(String.class, "name")
                .addModifiers(Modifier.PRIVATE)
                .build();
        MethodSpec getName = MethodSpec.methodBuilder("getName")
                .addModifiers(Modifier.PRIVATE)
                //定义返回值类型
                .returns(String.class)
                //代码方法内添加通用代码
                .addStatement("return this.name")
                .build();

        MethodSpec setName = MethodSpec.methodBuilder("setName")
                .addModifiers(Modifier.PRIVATE)
                //参数接收,这里注意，如果是int 就直接写int不用装箱
                .addParameter(String.class, "name")
//                .addStatement("this.name = name")
                .addStatement("this.$N = $N", "name", "name")
                .build();

        TypeSpec dataBean = TypeSpec.classBuilder("DataBean")
                .addField(name)
                .addModifiers(Modifier.STATIC)
                .addMethod(getName)
                .addMethod(setName)
                .addJavadoc("内部类生成")
                .build();

        //使用 TypeSpec 生成 BaseEntity 类
        TypeSpec BaseEntity = TypeSpec.classBuilder("BaseEntity")   //主类的名称
                .addField(code)
                .addField(msg)
                .addField(data, "data")
                .addMethod(getCode)
                .addMethod(setCode)
                .addType(dataBean)
                .addJavadoc("注释注解代码块")
                .build();


        try {
            JavaFile javaFile = JavaFile.builder("com.zbc.latte_compiler.javapoeatdemo", BaseEntity)
                    .build();
            /**
             * 代码写入控制台
             */
            javaFile.writeTo(System.out);
            /**
             * 代码写入文件 E:\\FastEc\\latte_compiler\\src\\main\\java
             */
            File file = new File("latte_compiler\\src\\main\\java");
            System.out.println("___" + file.getAbsolutePath());
            javaFile.writeTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
