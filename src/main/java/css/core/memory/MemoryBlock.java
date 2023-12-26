package css.core.memory;
public class MemoryBlock {
    private String content;

    public MemoryBlock() {
        this.content = "---"; // 空块的默认内容
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
