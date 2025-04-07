package Classes;

import java.io.Serializable;
import java.util.Objects;

public class ForumHasTagId implements Serializable {
    private Long forumId;
    private Integer tagId;

    public ForumHasTagId() {}

    public ForumHasTagId(Long forumId, Integer tagId) {
        this.forumId = forumId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumHasTagId that = (ForumHasTagId) o;
        return Objects.equals(forumId, that.forumId) &&
                Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forumId, tagId);
    }
}
