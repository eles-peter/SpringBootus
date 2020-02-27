package creator.backend.exception;

import creator.DatabaseService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateGlobalExceptionHandler {

    private CreateGlobalExceptionHandler() {
    }

    public static void createGlobalExceptionHandler(DatabaseService databaseService) {
        File file= new File(databaseService.getBackendApplicationDirectory() + "\\exception\\GlobalExceptionHandler.java");
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
                    "import com.fasterxml.jackson.core.JsonParseException;\n" +
                    "import org.slf4j.Logger;\n" +
                    "import org.slf4j.LoggerFactory;\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.context.MessageSource;\n" +
                    "import org.springframework.http.HttpStatus;\n" +
                    "import org.springframework.http.ResponseEntity;\n" +
                    "import org.springframework.validation.BindingResult;\n" +
                    "import org.springframework.validation.FieldError;\n" +
                    "import org.springframework.web.bind.MethodArgumentNotValidException;\n" +
                    "import org.springframework.web.bind.annotation.ControllerAdvice;\n" +
                    "import org.springframework.web.bind.annotation.ExceptionHandler;\n" +
                    "\n" +
                    "import java.util.List;\n" +
                    "import java.util.Locale;\n" +
                    "\n" +
                    "@ControllerAdvice\n" +
                    "public class GlobalExceptionHandler {\n" +
                    "\n" +
                    "    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);\n" +
                    "    private final MessageSource messageSource;\n" +
                    "\n" +
                    "    @Autowired\n" +
                    "    public GlobalExceptionHandler(MessageSource messageSource) {\n" +
                    "        this.messageSource = messageSource;\n" +
                    "    }\n" +
                    "\n" +
                    "    @ExceptionHandler(MethodArgumentNotValidException.class)\n" +
                    "    protected ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {\n" +
                    "        logger.error(\"A validation error occurred: \", ex);\n" +
                    "        BindingResult result = ex.getBindingResult();\n" +
                    "        List<FieldError> fieldErrors = result.getFieldErrors();\n" +
                    "\n" +
                    "        return new ResponseEntity<>(processFieldErrors(fieldErrors), HttpStatus.BAD_REQUEST);\n" +
                    "    }\n" +
                    "\n" +
                    "    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {\n" +
                    "        ValidationError validationError = new ValidationError();\n" +
                    "\n" +
                    "        for (FieldError fieldError : fieldErrors) {\n" +
                    "            validationError.addFieldError(fieldError.getField(), messageSource.getMessage(fieldError, Locale.getDefault()));\n" +
                    "        }\n" +
                    "\n" +
                    "        return validationError;\n" +
                    "    }\n" +
                    "\n" +
                    "    @ExceptionHandler(JsonParseException.class)\n" +
                    "    public ResponseEntity<ApiError> handleJsonParseException(JsonParseException ex) {\n" +
                    "        logger.error(\"Request JSON could no be parsed: \", ex);\n" +
                    "        HttpStatus status = HttpStatus.BAD_REQUEST;\n" +
                    "\n" +
                    "        ApiError body = new ApiError(\"JSON_PARSE_ERROR\", \"The request could not be parsed as a valid JSON.\", ex.getLocalizedMessage());\n" +
                    "\n" +
                    "        return new ResponseEntity<>(body, status);\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    @ExceptionHandler(IllegalArgumentException.class)\n" +
                    "    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {\n" +
                    "        logger.error(\"Illegal argument error: \", ex);\n" +
                    "        HttpStatus status = HttpStatus.BAD_REQUEST;\n" +
                    "\n" +
                    "        ApiError body = new ApiError(\"ILLEGAL_ARGUMENT_ERROR\", \"An illegal argument has been passed to the method.\", ex.getLocalizedMessage());\n" +
                    "\n" +
                    "        return new ResponseEntity<>(body, status);\n" +
                    "    }\n" +
                    "\n" +
                    "    @ExceptionHandler(Throwable.class)\n" +
                    "    public ResponseEntity<ApiError> defaultErrorHandler(Throwable t) {\n" +
                    "        logger.error(\"An unexpected error occurred: \", t);\n" +
                    "        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;\n" +
                    "\n" +
                    "        ApiError body = new ApiError(\"UNCLASSIFIED_ERROR\", \"Oh, snap! Something really unexpected occurred.\", t.getLocalizedMessage());\n" +
                    "\n" +
                    "        return new ResponseEntity<>(body, status);\n" +
                    "    }\n" +
                    "\n" +
                    "}");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
