package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.OrganizationalUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationalUnitService {
    private final OrganizationalUnitRepository organizationalUnitRepository;

    public OrganizationalUnitService(OrganizationalUnitRepository organizationalUnitRepository) {
        this.organizationalUnitRepository = organizationalUnitRepository;
    }
    public OrganizationalUnit addOrganizationalUnit(OrganizationalUnit organizationalUnit){

        return organizationalUnitRepository.save(organizationalUnit);
    }
    public List<OrganizationalUnit> findAllOrganizationalUnit(){
        return organizationalUnitRepository.findAll();
    }
    public OrganizationalUnit  updateOrganizationalUnit(OrganizationalUnit organizationalUnit){
        return organizationalUnitRepository.save(organizationalUnit);
    }
    public OrganizationalUnit findOrganizationalUnitById(Long id){
        return organizationalUnitRepository.findOrganizationalUnitById(id)
                .orElseThrow(()-> new UserNotFoundException("OrganizationalUnit by id" + id + " was not found"));
    }

    public List<OrganizationalUnit> findOrganizationalUnitBySubProcess(SubProcess subProcess){
        return organizationalUnitRepository.findOrganizationalUnitBySubProcess(subProcess);
    }
    public String deleteOrganizationalUnit(Long id){

        organizationalUnitRepository.deleteById(id);
        return  "Record deleted Succesfully";
    }

    public OrganizationalUnit findOrganizationalUnitByName(String name) {
        return organizationalUnitRepository.findOrganizationalUnitByName(name).orElseThrow(()-> new UserNotFoundException("OrganizationalUnit by name=" + name + " was not found"));
    }
}
