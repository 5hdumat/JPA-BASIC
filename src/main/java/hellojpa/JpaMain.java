package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        /**
         * EntityManagerFactory는 애플리케이션 로딩 시점에 단 1개만 생성한다. (DB당 1개)
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        /**
         * 트랜잭션 단위마다 EntityManager를 꼭 만들어줘야 한다. (클라이언트의 행위 단위마다)
         * 쉽게 말해 데이터베이스 커넥션 하나를 받았다고 생각하면 된다. (절대 쓰레드간에 공유 X, 사용하고 폐기해야 한다.)
         */
        EntityManager em = emf.createEntityManager();

        // 엔티티매니저로부터 트랜잭션을 얻어온다.
        EntityTransaction tx = em.getTransaction();

        /**
         * 트랜잭션을 시작한다.
         * (JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
         * 단, 단순 데이터 조회 같은건 트랜잭션을 선언하지 않아도 조회가 가능하긴하다.)
         */
        tx.begin();

        try {
            Member member = em.find(Member.class, 150L);
            member.setName("bw");

            /**
             * 영속성 컨택스트의 이점 (영속성 컨택스트는 고객의 요청 1:1 대응한다.)
             *
             * - 1차 캐시 (map 형태로 Key는 member 객체의 id, value는 member Entity)
             *   em.find(Member.class, "member1");과 같이 조회가 이루어 질 때 DB에서 곧바로 조회하는게 아닌
             *   영속성 컨택스트의 1차 캐시를 먼저 찾아본다. 그에 대응하는 키가 존재하면 캐시를 반환한다.
             *   1차 캐시에 존재하지 않으면 DB에서 조회한 후 DB에서 조회한 객체를 1차 캐시에 저장한 후 객체를 반환한다.
             *
             * - 동일성 보장
             *   '==' 비교를 했을 때 동일한 엔티티라면 같은 reference를 갖는다.
             *
             * - 엔티티 등록
             *   JPA는 1차 캐시 말고도 트랜잭션을 지원하는 쓰기 지연 SQL 저장소가 존재한다.
             *   엔티티를 persist하게 되면 위 저장소에 저장이 되는데, hibernate.jdbc.batch_size 옵션에
             *   등록된 사이즈에 따라 엔티티를 버퍼에 모아서 한번에 insert 할 수 있다.
             *
             * - 엔티티 수정(변경 감지, Dirty Checking)
             */

            /**
             * 비영속 상태
             */
            //            Member member = new Member();
            //            member.setId(101L);
            //            member.setName("helloJPA");

            /**
             * 영속 상태 (em 안에 있는 영속성 컨택스트에서 member 객체가 관리된다.)
             * 중요한건 이 시점에서 DB에 저장되는게 아니다.
             *
             * - em.detach(member); // 영속성 컨택스트에서 member 객체를 분리, 준영속 상태
             * - em.remove(member); // 객체를 삭제한 상태(삭제) 트랜잭션이 커밋될 때 DB 삭제 쿼리가 실행된다.
             */
            //            System.out.println("=== BEFORE ===");
            //            em.persist(member);
            //            System.out.println("=== AFTER ===");

            //            // 실제 쿼리로 조회
            //            Member findMember1 = em.find(Member.class, 101L);
            //            System.out.println(findMember1.getId());
            //            System.out.println(findMember1.getName());
            //
            //            // 1차 캐시에서 조회
            //            Member findMember2 = em.find(Member.class, 101L);
            //            System.out.println(findMember2.getName());
            //            System.out.println(findMember2.getName());
            //
            //            // 동일성 보장 (1차 캐시가 있기때문에 동일성이 보장)
            //            // 1차캐시로 이터러블한 읽기 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공한다.
            //            System.out.println(findMember1 == findMember2); // true

            /**
             * JPA 입장에서 쿼리를 작성할 때 RDB의 테이블을 대상으로 절대 쿼리를 작성하지 않는다.
             * 이렇게 하지 않음으로써 RDB에 종속적인 설계를 하지 않아도 된다.
             *
             * 아래의 예제에선 Member 객체를 대상으로 쿼리를 작성한다.
             * (Member 객체를 모두 가져오라는 의미)
             *
             * 즉, JQPL은 SQL을 추상화한 객체 지향 쿼리라고 보면 된다.
             * (SQL과 문법이 유사해서 일반적인 RDB SQL인 것 같지만 아니다.)
             *
             * [정리]
             * - JPQL: 엔티티 객체를 대상으로 쿼리
             * (JPQL을 실행하면 persistence.xml에 설정된 RDB에 따른 데이터베이스 방언과 합쳐서 RDB에 맞는 쿼리가 실행된다.)
             * - SQL: 데이터베이스 테이블을 대상으로 쿼리
             */
            //            List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
            //                    .setFirstResult(1)
            //                    .setMaxResults(8)
            //                    .getResultList();
            //
            //            for (Member member : resultList) {
            //                System.out.println(member.getName());
            //            }

            // 트랜잭션을 커밋한다. (이 시점에서 DB에 쿼리가 실행된다.)
            tx.commit();
        } catch (Exception e) {
            // 예외가 발생하면 트랜잭션을 롤백한다.
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
