package madcamp4.Our_Beloved_KAIST.Dto;

import java.util.List;

public class MarbleRequest {
    private String content;       // "content" 필드
    private List<String> mediaUrls;  // "mediaUrls" 필드 (optional)

    // 기본 생성자
    public MarbleRequest() {
    }

    // 매개변수 있는 생성자 (선택 사항)
    public MarbleRequest(String content, List<String> mediaUrls) {
        this.content = content;
        this.mediaUrls = mediaUrls;
    }

    // Getter와 Setter 메소드
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
}

