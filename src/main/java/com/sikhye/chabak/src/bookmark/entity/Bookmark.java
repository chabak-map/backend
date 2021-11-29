package com.sikhye.chabak.src.bookmark.entity;

import com.sikhye.chabak.base.entity.BaseEntity;
import com.sikhye.chabak.base.entity.BaseStatus;
import com.sikhye.chabak.src.member.entity.Member;
import com.sikhye.chabak.src.place.entity.Place;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
@Table(name = "Bookmark")
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "member_id")
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;

    @Builder
    public Bookmark(Long placeId, Long memberId) {
        this.placeId = placeId;
        this.memberId = memberId;
    }

    public void setStatusToUsed() {
        this.status = BaseStatus.used;
    }

    public void setStatusToDelete() {
        this.status = BaseStatus.deleted;
    }

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    //	// 연관관계 지원 함수
//	public void changePlace(Place place) {
//		this.place = place;
//		team.getPlaces().add(this);	// 리스트에 자신을 추가
//	}


}
