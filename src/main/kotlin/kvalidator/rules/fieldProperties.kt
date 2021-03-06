package kvalidator.rules

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject

class Required : Rule() {
    override val name: String = "required"
    override fun validate(data: JsonObject?, attribute: String): Boolean {
        if (data == null) return false
        if (!data.containsKey(attribute)) return false

        return when (val element = data[attribute]) {
            is JsonNull -> false
            is JsonLiteral -> {
                when {
                    element.isString -> element.content.length.toDouble() > 0
                    else -> true
                }
            }
            is JsonArray -> {
                when {
                    element.isNotEmpty() -> true
                    else -> false
                }
            }
            is JsonObject -> {
                when {
                    element.isNotEmpty() -> true
                    else -> false
                }
            }
            else -> false
        }
    }
}

class Confirmed : Rule() {
    override val name: String = "confirmed"
    override fun validate(data: JsonObject?, attribute: String): Boolean {
        if (data == null) return false
        if (!data.containsKey(attribute)) return false

        val confirmAttr = attribute + "_confirmation"
        if (!data.containsKey(confirmAttr)) return false
        val elementConfirm = data[confirmAttr]

        return when (val element = data[attribute]) {
            is JsonLiteral -> {
                when (element) {
                    elementConfirm -> true
                    else -> false
                }
            }
            else -> false
        }
    }
}

class Accepted : Rule() {
    override val name: String = "accepted"
    override fun validate(data: JsonObject?, attribute: String): Boolean {
        if (data == null) return false
        if (!data.containsKey(attribute)) return false

        return when (val element = data[attribute]) {
            is JsonLiteral -> {
                when {
                    element.isString && element.content == "true" -> true
                    element.isString && element.content == "yes" -> true
                    element.isString && element.content == "on" -> true
                    element.isString && element.content == "1" -> true
                    element.contentOrNull == "true" -> true
                    element.doubleOrNull != null && element.double == 1.00 -> true
                    else -> false
                }
            }
            else -> false
        }
    }
}

