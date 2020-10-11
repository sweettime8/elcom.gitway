/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.repository;

import com.elcom.gateway.model.dto.IpInformation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface IpInformationRepository extends PagingAndSortingRepository<IpInformation, Long> {
    
}
