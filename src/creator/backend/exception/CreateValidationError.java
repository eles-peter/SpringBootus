package creator.backend.exception;

import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateValidationError {

    private CreateValidationError() {
    }

    public static void createValidationError(DatabaseService databaseService) {
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\exception\\ValidationError.java");
        String filePackageName = databaseService.getProjectName() + ".exception";

        try (FileWriter writer = new FileWriter(file)) {

            writer.write("/*\n" +
                    " * Copyright © Progmasters (QTC Kft.), 2016-2019.\n" +
                    " * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,\n" +
                    " * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including\n" +
                    " * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.\n" +
                    " * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s\n" +
                    " * students and for no other purposes by any parties other than QTC Kft.\n" +
                    " * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.\n" +
                    " * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.\n" +
                    " */\n" +
                    "\n");

            writer.write("package " + filePackageName + ";\n\n");

            writer.write("\n" +
                    "import java.util.ArrayList;\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "public class ValidationError {\n" +
                    "    private List<CustomFieldError> fieldErrors = new ArrayList<>();\n" +
                    "\n" +
                    "    void addFieldError(String field, String message) {\n" +
                    "        CustomFieldError error = new CustomFieldError(field, message);\n" +
                    "        fieldErrors.add(error);\n" +
                    "    }\n" +
                    "\n" +
                    "    public List<CustomFieldError> getFieldErrors() {\n" +
                    "        return fieldErrors;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setFieldErrors(List<CustomFieldError> customFieldErrors) {\n" +
                    "        this.fieldErrors = customFieldErrors;\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    private static class CustomFieldError {\n" +
                    "\n" +
                    "        private String field;\n" +
                    "        private String message;\n" +
                    "\n" +
                    "        CustomFieldError(String field, String message) {\n" +
                    "            this.field = field;\n" +
                    "            this.message = message;\n" +
                    "        }\n" +
                    "\n" +
                    "        public String getField() {\n" +
                    "            return field;\n" +
                    "        }\n" +
                    "\n" +
                    "        public void setField(String field) {\n" +
                    "            this.field = field;\n" +
                    "        }\n" +
                    "\n" +
                    "        public String getMessage() {\n" +
                    "            return message;\n" +
                    "        }\n" +
                    "\n" +
                    "        public void setMessage(String message) {\n" +
                    "            this.message = message;\n" +
                    "        }\n" +
                    "    }\n" +
                    "}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
