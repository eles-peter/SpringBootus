package creator.backend.config;

import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateSpringWebConfig {

    private CreateSpringWebConfig() {
    }

    public static void createSpringWebConfig(DatabaseService databaseService) {
        String fileName = "SpringWebConfig";
        File file = new File(databaseService.getBackendApplicationDirectory() + "\\config\\" + fileName + ".java");
        String filePackageName = databaseService.getProjectName() + ".config";

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("package " + filePackageName + ";\n");
            writer.write("\n" +
                    "import org.springframework.context.MessageSource;\n" +
                    "import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                    "import org.springframework.context.support.ReloadableResourceBundleMessageSource;\n" +
                    "import org.springframework.web.servlet.config.annotation.CorsRegistry;\n" +
                    "import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;\n" +
                    "\n" +
                    "\n" +
                    "@Configuration\n" +
                    "public class SpringWebConfig implements WebMvcConfigurer {\n" +
                    "\n" +
                    "    @Bean\n" +
                    "    public MessageSource messageSource() {\n" +
                    "        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();\n" +
                    "        messageSource.setBasename(\"classpath:messages\");\n" +
                    "        messageSource.setDefaultEncoding(\"UTF-8\");\n" +
                    "        return messageSource;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void addCorsMappings(CorsRegistry registry) {\n" +
                    "        registry.addMapping(\"/**\")\n" +
                    "                .allowedOrigins(\"http://localhost:3000\", \"http://localhost:4200\")\n" +
                    "                .allowedMethods(\"GET\", \"POST\", \"DELETE\", \"PUT\");\n" +
                    "    }\n" +
                    "\n" +
                    "}");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
