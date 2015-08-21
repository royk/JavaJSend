import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSend {

    protected static String UNKNOWN_ERROR_MESSAGE = "An unknown error occurred";

    private Status status;
    private JsonElement data;

    public static String success() {
        return success(null);
    }

    public static String fail() {
        return fail(null);
    }

    public static String success(JsonElement data) {
        return new JSend(Status.success, data).toString();
    }

    public static JsonObject addFailure(JsonObject o, String field, String error) {
        if (o==null) {
            o = new JsonObject();
        }
        o.addProperty(field, error);
        return o;
    }

    public static String fail(JsonObject data) {
        return new JSend(Status.fail, data).toString();
    }

    public static String error(String message) {
        return new JSend(Status.error, message).toString();
    }

    public static String error(String message, String code) {
        JsonObject data = new JsonObject();
        data.addProperty("message", message);
        data.addProperty("code", code);
        return new JSend(Status.error, data).toString();
    }

    public JSend() {}

    public JSend(Status status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    public JSend(Status status, String message) {
        this.status = status;
        this.data = new JsonObject();
        ((JsonObject)data).addProperty("message", message);
    }

    public JsonObject getAsJSon() {
        JsonObject response = new JsonObject();
        response.addProperty("status", status.name());
        if (status!=Status.error) {
            if (data==null) {
                data = new JsonObject();
            }
            response.add("data", data);
        } else {
            if (data==null || ! ((JsonObject)data).has("message")) {
                data = new JsonObject();
                ((JsonObject)data).addProperty("message", JSend.UNKNOWN_ERROR_MESSAGE);
            }
            response.add("message",  ((JsonObject)data).get("message"));
            if ( ((JsonObject)data).has("code")) {
                response.add("code",  ((JsonObject)data).get("code"));
            }
        }
        return response;
    }

    @Override
    public String toString() {
        return getAsJSon().toString();
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        success,
        fail,
        error
    }
}
