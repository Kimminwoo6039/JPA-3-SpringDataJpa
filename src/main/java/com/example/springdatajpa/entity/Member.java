package com.example.springdatajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 proteted 같은거
@ToString(of = {"id","username","age"}) // team 쓰면 연관관계 까지 출력됨...... 가급적이면 연관관계는 안쓰자..
public class Member {

    @Id @GeneratedValue // pk 값을 jpa 가 알아서 해줌
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age , Team team) {
        this.username= username;
        this.age = age;
        if (team !=null) {
            changeTeam(team);
        }
    }


    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
