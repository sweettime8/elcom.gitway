/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.service.impl;

import com.elcom.gateway.model.dto.IpInformation;
import com.elcom.gateway.repository.IpInformationCustomizeRepository;
import com.elcom.gateway.repository.IpInformationRepository;
import com.elcom.gateway.service.IpInformationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class IpInformationServiceImpl implements IpInformationService  {
    
    private final IpInformationRepository ipRepository;
    private final IpInformationCustomizeRepository ipCustomizeRepository;

    @Autowired
    public IpInformationServiceImpl(IpInformationRepository ipRepository, IpInformationCustomizeRepository ipCustomizeRepository) {
        this.ipRepository = ipRepository;
        this.ipCustomizeRepository = ipCustomizeRepository;
    }
    
    
    
    @Override
    public List<IpInformation> findAll(Integer currentPage, Integer rowsPerPage, String sort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IpInformation findById(Long id) {
        return ipCustomizeRepository.findById(id);
    }

    @Override
    public void save(IpInformation ipInfor) {
        ipRepository.save(ipInfor);
    }

    @Override
    public void remove(IpInformation ipInfor) {
        ipRepository.delete(ipInfor);}

    @Override
    public boolean insertTest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
