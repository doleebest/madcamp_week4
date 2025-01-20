package madcamp4.Our_Beloved_KAIST.Dto;

public class ItemRequest {
    private String type; // "TEXT", "PHOTO", "VIDEO"
    private String content;

    // Getters
    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }
}