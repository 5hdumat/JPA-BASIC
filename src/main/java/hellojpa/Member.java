package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
/**
 * 애플리케이션 단에서의 객체는 Member지만 RDB 테이블이 USER 일 경우
 * @Table(name ="USER") 애노테이션을 통해 쿼리문의 테이블명을 수정할 수 있다.
 */
public class Member {
    public Member() {
    }

    public Member(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    private long id;

    /**
     * 컬럼도 마찬가지로 하단의 애노테이션을 통해 쿼리문의 컬럼명을 수정할 수 있다.
     *
     * @Column(name = "username")
     */
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
