package creator.backend.exception;

import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateApiError {

    private CreateApiError() {
    }

    public static void createApiError(DatabaseService databaseService) {
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\exception\\ApiError.java");
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
                    "public class ApiError {\n" +
                    "    private String errorCode;\n" +
                    "    private String error;\n" +
                    "    private String details;\n" +
                    "\n" +
                    "    public ApiError(String errorCode, String error, String details) {\n" +
                    "        this.errorCode = errorCode;\n" +
                    "        this.error = error;\n" +
                    "        this.details = details;\n" +
                    "    }\n" +
                    "\n" +
                    "    public String getErrorCode() {\n" +
                    "        return errorCode;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setErrorCode(String errorCode) {\n" +
                    "        this.errorCode = errorCode;\n" +
                    "    }\n" +
                    "\n" +
                    "    public String getError() {\n" +
                    "        return error;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setError(String error) {\n" +
                    "        this.error = error;\n" +
                    "    }\n" +
                    "\n" +
                    "    public String getDetails() {\n" +
                    "        return details;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setDetails(String details) {\n" +
                    "        this.details = details;\n" +
                    "    }\n" +
                    "}");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
