package org.example.fourtreesproject.bid.repository;

import org.example.fourtreesproject.bid.model.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
}