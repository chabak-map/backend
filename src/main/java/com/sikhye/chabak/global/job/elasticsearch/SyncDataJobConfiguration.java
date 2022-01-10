// package com.sikhye.chabak.global.job.config;
//
// import java.util.List;
//
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
// import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
// import org.springframework.batch.repeat.RepeatStatus;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// import com.sikhye.chabak.service.place.PlaceService;
// import com.sikhye.chabak.service.post.PostingService;
// import com.sikhye.chabak.service.post.domain.Posting;
// import com.sikhye.chabak.service.search.SearchService;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Configuration
// public class SyncDataJobConfiguration {
//
// 	private final JobBuilderFactory jobBuilderFactory;
// 	private final StepBuilderFactory stepBuilderFactory;
// 	private final PostingService postingService;
// 	private final PlaceService placeService;
// 	private final SearchService searchService;
//
// 	public SyncDataJobConfiguration(
// 		JobBuilderFactory jobBuilderFactory,
// 		StepBuilderFactory stepBuilderFactory, PostingService postingService,
// 		PlaceService placeService, SearchService searchService) {
// 		this.jobBuilderFactory = jobBuilderFactory;
// 		this.stepBuilderFactory = stepBuilderFactory;
// 		this.postingService = postingService;
// 		this.placeService = placeService;
// 		this.searchService = searchService;
// 	}
//
// 	/*
// 		1) DB에 있는 데이터를 읽음 (use findAll)
// 		2)
// 	 */
//
// 	@Bean
// 	public Job syncDataJob() {
// 		return jobBuilderFactory.get("syncDataJob")
// 			.start(readDataStep())
// 			// .next(saveDataStep())
// 			.build();
// 	}
//
// 	@Bean
// 	public Step readDataStep() {
// 		return this.stepBuilderFactory.get("readDataStep")
// 			.tasklet((contribution, chunkContext) -> {
// 				syncPosts();
// 				return RepeatStatus.FINISHED;
// 			})
// 			.build();
// 	}
//
// 	// private void syncPlaces() {
// 	// 	// Specification<Place> userSpecification = (root, criteriaQuery, criteriaBuilder) ->
// 	// 	// 	getModificationDatePredicate(criteriaBuilder, root);
// 	// 	List<Place> placeList;
// 	// 	if (placeService.countPlaces() == 0) {
// 	// 		placeList = placeService.findPlaces();
// 	// 	} else {
// 	// 		placeList = placeService.findPlaces();
// 	// 	}
// 	//
// 	// 	log.info("<< ++++++++++++++++++ >> 추출한 내용 저장");
// 	// 	placeList.forEach(place -> {
// 	// 		searchService.addPlace()
// 	// 	});
// 	// 	log.info("<< ++++++++++++++++++ >> 추출한 내용 저장 완료");
// 	//
// 	// 	// for (User user : placeList) {
// 	// 	// 	LOG.info("Syncing User - {}", user.getId());
// 	// 	// 	userESRepo.save(this.userMapper.toUserModel(user));
// 	// 	// }
// 	// }
//
// 	private void syncPosts() {
// 		// Specification<Place> userSpecification = (root, criteriaQuery, criteriaBuilder) ->
// 		// 	getModificationDatePredicate(criteriaBuilder, root);
// 		List<Posting> postingList;
// 		if (postingService.countPosts() == 0) {
// 			postingList = postingService.findPostings();
// 		} else {
// 			postingList = postingService.findPostings();
// 		}
//
// 		log.info("<< ++++++++++++++++++ >> 추출한 내용 저장");
// 		postingList.forEach(posting -> {
// 			String s = searchService.addPost(posting);
// 			log.info("포스팅명: {}", s);
// 		});
// 		log.info("<< ++++++++++++++++++ >> 추출한 내용 저장 완료");
//
// 		// for (User user : placeList) {
// 		// 	LOG.info("Syncing User - {}", user.getId());
// 		// 	userESRepo.save(this.userMapper.toUserModel(user));
// 		// }
// 	}
//
// 	// private static Predicate getModificationDatePredicate(CriteriaBuilder cb, Root<?> root) {
// 	// 	Expression<Timestamp> currentTime;
// 	// 	currentTime = cb.currentTimestamp();
// 	// 	Expression<Timestamp> currentTimeMinus = cb.literal(
// 	// 		new Timestamp(System.currentTimeMillis() -
// 	// 			(Constants.INTERVAL_IN_MILLISECONDE)));
// 	// 	return cb.between(root.<Date>get(Constants.MODIFICATION_DATE),
// 	// 		currentTimeMinus,
// 	// 		currentTime
// 	// 	);
// 	// }
// }
