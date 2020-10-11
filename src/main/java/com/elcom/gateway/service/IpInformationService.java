/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.service;

import com.elcom.gateway.model.dto.IpInformation;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface IpInformationService {

    List<IpInformation> findAll(Integer currentPage, Integer rowsPerPage, String sort);

    IpInformation findById(Long id);

    void save(IpInformation ipInfor);

    void remove(IpInformation ipInfor);

    boolean insertTest();
}
