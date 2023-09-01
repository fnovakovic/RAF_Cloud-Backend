package rs.raf.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.*;
import rs.raf.demo.services.ErrorService;
import rs.raf.demo.services.MachineService;
import rs.raf.demo.services.User2Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService machineService;
    private final ErrorService errorService;
    private  User2Service user2Service;
    private User2 currUser;
    private StatusEnum statusEnum;
    private final PasswordEncoder passwordEncoder;


    public MachineController(ErrorService errorService,MachineService machineService,User2Service user2Service, PasswordEncoder passwordEncoder) {
        this.errorService = errorService;
        this.machineService = machineService;
        this.user2Service = user2Service;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping(value = "/all",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Machine>> getAllMachines(@RequestBody Machine machine){
        currUser =   user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getSearchMachine().equals("1")){ //Provera da li ima permisiju
            List<Machine> lista2 = machineService.findById(currUser);//currUser.getUserId()
            return ResponseEntity.ok(lista2);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping(value = "/allErrors",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ErrorMessage>> getAllErrors(@RequestBody Machine machine){
        currUser =   user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            List<ErrorMessage> lista2 = errorService.find(currUser.getUserId());
            return ResponseEntity.ok(lista2);
    }

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createMachine(@RequestBody Machine mchRequest){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getCreateMachine().equals("1")){//Provera da li ima permisiju
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                String str = formatter.format(date);
                System.out.print("Current date: "+str);
            Date date1 = null;
            try {
                date1=new SimpleDateFormat("dd/MM/yy").parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Machine mch = new Machine();
                mch.setStatus(statusEnum.STOPPED);
                mch.setActive(Boolean.FALSE);
//                mch.setCreatedBy(currUser.getUserId());
                mch.setCreatedBy(currUser);
                mch.setName(mchRequest.getName());
                mch.setDestroy(Boolean.FALSE);
                mch.setDate(date1);

                return ResponseEntity.ok(machineService.save(mch));
            }
        return ResponseEntity.status(403).build();
    }

    @PatchMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> destroyMachine(@RequestBody Machine mach){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getDestroyMachine().equals("1")) {//Provera da li ima permisiju
            machineService.deleteByName(mach.getId(), StatusEnum.STOPPED);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PatchMapping(value = "/start",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> start(@RequestBody Machine mach){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getStartMachine().equals("1")) {//Provera da li ima permisiju
            machineService.startMachine(mach.getId(), StatusEnum.RUNNING, StatusEnum.STOPPED);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PatchMapping(value = "/reservedStart",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reservedStart(@RequestBody Reserved res){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getStartMachine().equals("1")) {//Provera da li ima permisiju
            machineService.reservedStart(res.getId(), res.getDateAndTime(), StatusEnum.RUNNING, StatusEnum.STOPPED);
            return ResponseEntity.ok().build();
        }else {//AKO NEMA PRAVIMO GRESKU
            ErrorMessage err = new ErrorMessage();
            err.setMessage("Neuspela rezervacija start masine,nemate start permisiju");
            err.setOperation("Reserved Start");

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            String str = formatter.format(date);
            Date date1 = null;
            try {
                date1 = new SimpleDateFormat("dd/MM/yy").parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            err.setDate(date1);
            err.setMachineId(res.getId());
            machineService.saveError(err);
        }
        return ResponseEntity.status(403).build();
    }


    @PatchMapping(value = "/stop",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stop(@RequestBody Machine mach){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getStopMachine().equals("1")) {//Provera da li ima permisiju
            machineService.stopMachine(mach.getId(), StatusEnum.STOPPED, StatusEnum.RUNNING);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PatchMapping(value = "/reservedStop",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reservedStop(@RequestBody Reserved res){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getStopMachine().equals("1")) {//Provera da li ima permisiju
            machineService.reservedStop(res.getId(), res.getDateAndTime(),StatusEnum.STOPPED, StatusEnum.RUNNING);
            return ResponseEntity.ok().build();
        }else {//AKO NEMA PRAVIMO GRESKU
        ErrorMessage err = new ErrorMessage();
        err.setMessage("Neuspela rezervacija stop masine,nemate stop permisiju");
        err.setOperation("Reserved Stop");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String str = formatter.format(date);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yy").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        err.setDate(date1);
        err.setMachineId(res.getId());
        machineService.saveError(err);
    }
        return ResponseEntity.status(403).build();
    }

    @PatchMapping(value = "/restart",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restart(@RequestBody Machine mach){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getRestartMachine().equals("1")) {//Provera da li ima permisiju
            machineService.restartMachine(mach.getId(),StatusEnum.RUNNING,StatusEnum.STOPPED);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PatchMapping(value = "/reservedRestart",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reservedRestart(@RequestBody Reserved res){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getRestartMachine().equals("1")) {//Provera da li ima permisiju
            machineService.reservedRestart(res.getId(), res.getDateAndTime(),StatusEnum.RUNNING,StatusEnum.STOPPED);
            return ResponseEntity.ok().build();
        } else {//AKO NEMA PRAVIMO GRESKU
        ErrorMessage err = new ErrorMessage();
        err.setMessage("Neuspela rezervacija restart masine,nemate restart permisiju");
        err.setOperation("Reserved Restart");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String str = formatter.format(date);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yy").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        err.setDate(date1);
        err.setMachineId(res.getId());
        machineService.saveError(err);
    }
        return ResponseEntity.status(403).build();
    }

    @PostMapping(value = "/search",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Machine>> searchMachine(@RequestBody Search src){
        currUser =  user2Service.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currUser.getSearchMachine().equals("1")) {//Provera da li ima permisiju

            if (!src.getName().equals("") && src.getStatus().equals("") && src.getDateFrom().equals("") && src.getDateTo().equals("")) {
                System.out.println("Usao u name ");
                List<Machine> lista2 = machineService.findByName(src.getName(), currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getStatus().equals("") && src.getName().equals("") && src.getDateFrom().equals("") && src.getDateTo().equals("")) {
                System.out.println("Usao je u status");
                List<Machine> lista2 = machineService.findByStatus(StatusEnum.valueOf(src.getStatus()), currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getDateFrom().equals("") && src.getStatus().equals("") && src.getName().equals("") && src.getDateTo().equals("")) {
                System.out.println("Usao je u dateFrom ");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByDateFrom(date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getDateTo().equals("") && src.getDateFrom().equals("") && src.getStatus().equals("") && src.getName().equals("")) {
                System.out.println("Usao je u dateTo ");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByDateTo(date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getStatus().equals("") && !src.getName().equals("") && src.getDateFrom().equals("") && src.getDateTo().equals("")) {
                System.out.println("Usao u name i status");
                List<Machine> lista2 = machineService.findByStatusAndName(StatusEnum.valueOf(src.getStatus()), src.getName(), currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getDateFrom().equals("") && !src.getName().equals("") && src.getStatus().equals("") && src.getDateTo().equals("")) {
                System.out.println("Usao u name i dateFrom ");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByNameAndDateFrom(src.getName(), date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getDateTo().equals("") && !src.getName().equals("") && src.getStatus().equals("") && src.getDateFrom().equals("")) {
                System.out.println("Usao u name i dateTo ");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByNameAndDateTo(src.getName(), date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getDateTo().equals("") && !src.getName().equals("") && src.getStatus().equals("") && !src.getDateFrom().equals("")) {
                System.out.println("Usao u name i dateFrom i dateTo ");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                    date2 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByNameAndDateFromAndDateTo(src.getName(), date1, date2, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getStatus().equals("") && !src.getDateFrom().equals("") && src.getName().equals("") && src.getDateTo().equals("")) {
                System.out.println("Usao u status i dateFrom");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByStatusAndDateFrom(StatusEnum.valueOf(src.getStatus()), date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getStatus().equals("") && !src.getDateTo().equals("") && src.getName().equals("") && src.getDateFrom().equals("")) {
                System.out.println("Usao u status i dateTo");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByStatusAndDateTo(StatusEnum.valueOf(src.getStatus()), date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getDateTo().equals("") && src.getName().equals("") && !src.getStatus().equals("") && !src.getDateFrom().equals("")) {
                System.out.println("Usao u status i dateFrom i dateTo ");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                    date2 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByStatusAndDateFromAndDateTo(StatusEnum.valueOf(src.getStatus()), date1, date2, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getDateFrom().equals("") && !src.getDateTo().equals("") && src.getName().equals("") && src.getStatus().equals("")) {
                System.out.println("Usao u dateFrom i dateTo");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                    date2 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByDateFromAndDateTo(date1, date2, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getStatus().equals("") && !src.getDateFrom().equals("") && !src.getName().equals("") && src.getDateTo().equals("")) {
                System.out.println("usao u status,dateFrom i name ");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByStatusAndNameAndDateFrom(StatusEnum.valueOf(src.getStatus()), src.getName(), date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getStatus().equals("") && src.getDateFrom().equals("") && !src.getName().equals("") && !src.getDateTo().equals("")) {
                System.out.println("usao u status,dateTo i name ");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByStatusAndNameAndDateTo(StatusEnum.valueOf(src.getStatus()), src.getName(), date1, currUser);
                return ResponseEntity.ok(lista2);

            } else if (!src.getStatus().equals("") && !src.getDateFrom().equals("") && !src.getName().equals("") && !src.getDateTo().equals("")) {
                System.out.println("usao u status,dateFrom,dateTo i name ");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateFrom());
                    date2 = new SimpleDateFormat("dd/MM/yy").parse(src.getDateTo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Machine> lista2 = machineService.findByStatusAndNameAndDateFromAndDateTo(StatusEnum.valueOf(src.getStatus()), src.getName(), date1, date2, currUser);
                return ResponseEntity.ok(lista2);

            } else {
                System.out.println("Nije usao nigde");
                List<Machine> lista2 = machineService.findById(currUser);
                return ResponseEntity.ok(lista2);
            }
        }

        return ResponseEntity.status(403).build();
    }

}
