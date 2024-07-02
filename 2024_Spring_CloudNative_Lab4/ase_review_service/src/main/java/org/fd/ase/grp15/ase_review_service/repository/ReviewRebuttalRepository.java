package org.fd.ase.grp15.ase_review_service.repository;

import org.fd.ase.grp15.ase_review_service.entity.ReviewRebuttal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRebuttalRepository extends JpaRepository<ReviewRebuttal, ReviewRebuttal.ReviewRebuttalId> {

    public List<ReviewRebuttal> findAllByReviewerUsername(String reviewerUsername);

    public List<ReviewRebuttal> findAllByConferenceName(String conferenceName);

    public List<ReviewRebuttal> findAllById_ContributionId(Long contributionId);
}
