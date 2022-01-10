package hellojpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * [@Entity]
 *
 * @Entity가 붙은 클래스는 JPA가 관리하는 엔티티이다.
 * JPA를 사용해서 테이블과 매핑할 클래스는 @Entity가 필수.
 * <p>
 * 주의점
 * - 엔티티에는 Name 속성이 있는데 기본적으로 엔티티명 = name값이 된다.
 * - @Entity가 붙은 객체는 기본 생성자가 필수
 * - final 클래스, enum, interface, inner 클래스는 사용할 수 없다.
 * - DB에 저장하고 싶은 필드는 final 사용 X
 */

/**
 * 애플리케이션 단에서의 객체는 Member지만 RDB 테이블이 USER 일 경우
 *
 * @Table(name ="USER") 애노테이션을 통해 쿼리문의 테이블명을 수정할 수 있다.
 */
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Member {

    /**
     * @Id(직접 할당):
     * - 개발자가 직접 db 기본키를 세팅해주고 싶다면 @Id를 사용하면된다.
     * @GeneratedValue(자동 할당):
     * - Identity 전략
     * - @GeneratedValue(strategy = GenerationType.Identity)
     * - Identity의 단점은 기본키 생성을 전적으로 데이터베이스에 위임하므로
     * ID 값을 알려면 DB에 데이터가 INSERT 되어한다는 점이다.
     * 이러한 이유 때문에 Identity 전략을 사용하면 persist()를 사용하는 시점에
     * INSERT 쿼리가 날아간다.
     * - Identity는 쓰기 지연 저장소에 쿼리를 모아놓고 한번에 commit 하는 전략을 사용할 수 없다.
     * <p>
     * - Sequence 전략
     * - @GeneratedValue(strategy = GenerationType.Sequence)
     * - 유일한 값을 순서대로 생성하는 데이터베이스 시퀀스 오브젝트를 통해 값을 가져온 후 기본키로 지정한다.
     * - 값을 지정하지 않으면 기본 시퀀스가 설정된다.
     * - @SequenceGenerator 를 통해 시퀀스를 매핑할 수 있다.
     * <p>
     * - TABLE 전략
     * - @TableGenerator()
     * - 데이터베이스 시퀀스 흉내를 내는 테이블을 하나 생성해 위 전략들과 동일한 행위를 한다.
     * - 성능이 좋지않아 권장하지 않는다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    /**
     * [@Column]
     * <p>
     * - name -> String: Entity의 필드와 테이블 컬럼이 상이할 경우
     * - insertable -> bool: 컬럼이 추가되었을 때 등록할건지 말건지
     * - updatable -> bool: 컬럼이 추가/변경되었을 때 변경할건지 말건지
     * - nullable -> bool: Null을 허용할건지 말건지
     * - unique -> bool: 엔티티의 필드에 유니크 제약조건을 걸건지?
     * -> 유니크 제약조건 이름이 이상한 문자열로 반영되기에 실무에서 잘 사용하지 않는다.
     *
     * @Table(uniqueContraints = ) 를 사용하자.
     * - columnDefinition -> String: 컬럼 정의를 직접내릴 수 있다.  columnDefinition = "varchar(100) default 'EMPTY"
     * - length -> int: 문자길이 제약조건, String 타입에만 사용한다. (varchar 사이즈 변경)
     * - precision -> int: BigDecimal, BigInteger와 같은 큰 숫자, 정밀한 소수점을 다루어야할 때 사용한다.
     */
    @Column(name = "name", nullable = false)
    private String username;

    /**
     * 엔티티의 필드가 Interger일 경우 DB의 가장 적절한 타입이 선택된다.
     */
    private Integer age;

    /**
     * 엔티티의 Enum 타입 필드와 컬럼을 매핑하려면 Enumerated를 사용하면된다.
     */
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    /**
     * 자바의 Date 타입엔 날짜, 시간 개념을 분리하지 않지만,
     * DB의 날짜 타입은 날짜, 시간, 날짜+시간을 구분하므로 지정해줘야 한다. (과거버전)
     * <p>
     * LocalDate, LocalDateTime을 사용할 때는 생략(최신 하이버네이트 지원)
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    /**
     * DB의 VARCHAR를 넘어서는 큰 내용을 넣기 위해선 @Lob 애노테이션을 붙인다.
     */
    @Lob
    private String description;

    /**
     * DB와 관계없이 메모리에서만 계산하고 싶다거나 매핑하고 싶지 않을 경우엔
     *
     * @Transient를 사용한다.
     */
    @Transient
    private int temp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public Member() {
    }
}

/**
 * 위 엔티티의 경우 JPA가 아래와 같이 쿼리를 만든다.
 * <p>
 * create table Member (
 * id bigint not null,
 * age integer,
 * createdDate timestamp,
 * description clob, // 문자타입이면 clob 나머지(바이트 타입)은 blob
 * lastModifiedDate timestamp,
 * roleType varchar(255),
 * name varchar(255),
 * primary key (id)
 * )
 */
