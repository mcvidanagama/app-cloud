//Regex patterns
var ALLOWED_CHARACTERS_REGEX = "^[A-Za-z0-9_]+$"; //Only alphanumeric and underscore characters are allowed for content


//Environment key validation
function validateEnvKey(envKey){
    var envKeyRegex = new RegExp(ALLOWED_CHARACTERS_REGEX);
    var validator;
    if (!envKeyRegex.test(envKey)) {
        validator = {
            status: false,
            msg: "Invalid value for variable key:" + envKey + ", Valid characters are [A-Z, a-z, 0-9, _]."
        }
    } else {
        validator = {
            status: true,
            msg: "Environment variable key validated and successfully added."
        }
    }
    return validator;
}

function validateDbName(databaseName) {
    var databaseNameRegex = new RegExp(ALLOWED_CHARACTERS_REGEX);
    var validator;
    if (!databaseNameRegex.test(databaseName)) {
        validator = {
            status: false,
            msg: "Invalid value for database name. Only alphanumeric characters and underscore are allowed."
        }
    } else {
        validator = {
            status: true,
            msg: "Database name validated and successfully added."
        }
    }
    return validator;
}