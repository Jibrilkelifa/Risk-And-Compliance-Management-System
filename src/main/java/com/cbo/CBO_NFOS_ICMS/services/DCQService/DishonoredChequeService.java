package com.cbo.CBO_NFOS_ICMS.services.DCQService;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.models.DCQ.DishonoredCheque;
import com.cbo.CBO_NFOS_ICMS.repositories.DCQRepository.DishonoredChequeRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DishonoredChequeService {
    private final SubProcessService subProcessService;
    private final BranchService organizationalUnitService;
    private final DishonoredChequeRepository dishonoredChequeRepository;

    public DishonoredChequeService(SubProcessService subProcessService, BranchService organizationalUnitService, DishonoredChequeRepository dishonoredChequeRepository) {

        this.subProcessService = subProcessService;
        this.organizationalUnitService = organizationalUnitService;
        this.dishonoredChequeRepository = dishonoredChequeRepository;
    }

    public DishonoredCheque addDishonouredCheque(DishonoredCheque DishonoredCheque) {

        return dishonoredChequeRepository.save(DishonoredCheque);
    }

    public List<DishonoredCheque> findAllDishonouredCheque() {
        return dishonoredChequeRepository.findAll();
    }

    public DishonoredCheque updateDishonouredCheque(DishonoredCheque dishonoredCheque) {
        Optional<DishonoredCheque> optionalDsihonoured = dishonoredChequeRepository.findById(dishonoredCheque.getId());
        if (optionalDsihonoured.isPresent()) {
            DishonoredCheque existingDishonoured = optionalDsihonoured.get();
            existingDishonoured.setTin(dishonoredCheque.getTin());
            existingDishonoured.setNameOfBeneficiary(dishonoredCheque.getNameOfBeneficiary());
            existingDishonoured.setAmountInBirr(dishonoredCheque.getAmountInBirr());
            existingDishonoured.setAccountNumber(dishonoredCheque.getAccountNumber());
            existingDishonoured.setFullNameOfDrawer(dishonoredCheque.getFullNameOfDrawer());
            existingDishonoured.setDrawerAddress(dishonoredCheque.getDrawerAddress());
            existingDishonoured.setChequeType(dishonoredCheque.getChequeType());
            existingDishonoured.setChequeNumber(dishonoredCheque.getChequeNumber());


            return dishonoredChequeRepository.save(existingDishonoured);
        } else {
            throw new ResourceNotFoundException("Dishonoured cheque not found");
        }
    }

    public DishonoredCheque findDishonouredChequeById(Long id) {
        return dishonoredChequeRepository.findDishonouredChequeById(id)
                .orElseThrow(() -> new UserNotFoundException("dishounered cheque by id" + id + " was not found"));
    }

    public int findDishonouredChequeByAccountNumber(String accountNumber) {
        return (dishonoredChequeRepository.findDishonouredChequeByAccountNumber(accountNumber)).size();
    }

    public void deleteDishonouredCheque(Long id) {
        dishonoredChequeRepository.deleteById(id);
    }

    public List<DishonoredCheque> findAllDishonouredChequeInSpecificOrganizationalUnit(String branchid) {
        //Branch branch = organizationalUnitService.findBranchById(id);
        return dishonoredChequeRepository.findDishonouredChequeByBranchId(branchid);
    }

    //    public List<DishonoredCheque> findAllDishonouredChequeInSpecificSubProcess(Long id) {
//        SubProcess subProcess = subProcessService.findSubProcessById(id);
//        List<Branch> organizationalUnits = organizationalUnitService.findBranchBySubProcess(subProcess);
//        List<DishonoredCheque> dchques = new ArrayList<>();
//        for (int i = 0; i < organizationalUnits.size(); i++) {
//            List<DishonoredCheque> clmfjhd = dishonoredChequeRepository.findDishonouredChequeByBranch(organizationalUnits.get(i));
//            for (int j = 0; j < clmfjhd.size(); j++) {
//                dchques.add(dishonoredChequeRepository.findDishonouredChequeByBranch(organizationalUnits.get(i)).get(j));
//
//            }
//        }
//        return dchques;
//    }
    public List<DishonoredCheque> findAllDishonouredChequeInSpecificSubProcess(Long subProcessId) {
        //Branch branch = organizationalUnitService.findBranchById(id);
        return dishonoredChequeRepository.findDishonouredChequeBySubProcessId(subProcessId);
    }

    public int findDishonouredChequeCountForYear(int year) {
        List<DishonoredCheque> dishonoredCheques = findAllDishonouredCheque();
        int count = 0;
        for (DishonoredCheque dcq : dishonoredCheques) {
            String datePresented = dcq.getDatePresented();
            SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
            try {
                Date date = formatter.parse(datePresented);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int yearOfCheque = calendar.get(Calendar.YEAR);
                if (yearOfCheque == year) {
                    count++;
                }
            } catch (ParseException e) {
                // handle parse exception
            }
        }
        return count;
    }

    public int countDishonouredChequesThreeTimesInLastWeek() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        int count = 0;
        for (DishonoredCheque dcq : dishonoredChequeRepository.findByFrequencyGreaterThanEqual(3)) {
            LocalDate datePresented = LocalDate.parse(dcq.getDatePresented(), formatter);
            if (datePresented.isAfter(startDate) && datePresented.isBefore(endDate.plusDays(1))) {
                count++;
            }
        }
        return count;
    }
    public List<DishonoredCheque> getDishonouredChequesThreeTimesInLastWeek() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        List<DishonoredCheque> dishonoredCheques = new ArrayList<>();

        for (DishonoredCheque dcq : dishonoredChequeRepository.findByFrequencyGreaterThanEqual(3)) {
            LocalDate datePresented = LocalDate.parse(dcq.getDatePresented(), formatter);
            if (datePresented.isAfter(startDate) && datePresented.isBefore(endDate.plusDays(1))) {
                dishonoredCheques.add(dcq);
            }
        }

        return dishonoredCheques;
    }

//    public List<DishonoredCheque> getDishonouredChequesThreeTimesInLastWeek() {
//        LocalDate startDate = LocalDate.now().minusDays(7);
//        LocalDate endDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
//        List<DishonoredCheque> dishonoredCheques = new ArrayList<>();
//
//        for (DishonoredCheque dcq : dishonoredChequeRepository.findByFrequencyGreaterThanEqual(3)) {
//            LocalDate datePresented = LocalDate.parse(dcq.getDatePresented(), formatter);
//            if (datePresented.isAfter(startDate) && datePresented.isBefore(endDate.plusDays(1))) {
//                dishonoredCheques.add(dcq);
//            }
//        }
//
//        return dishonoredCheques;
//    }

}
