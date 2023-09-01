package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.*;

import rs.raf.demo.repositories.ErrorRepository;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.repositories.User2Repository;
import rs.raf.demo.repositories.userCrudRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MachineService {
    private MachineRepository machineRepository;
    private ErrorRepository errorRepository;
    private TaskScheduler taskScheduler;

    @Autowired
    public MachineService(MachineRepository machineRepository,ErrorRepository errorRepository) { //userCrudRepository userCrudRepository
        this.machineRepository = machineRepository;
        this.errorRepository = errorRepository;
    }


    public List<Machine> findById(User2 id) { //Long
        return machineRepository.findMachineByCreatedByAndDestroyAndDestroyIsFalse(id,false);
    }

    public Machine save(Machine mch) {
        return machineRepository.save(mch);
    }


    public void deleteByName(Long id,StatusEnum status) {
        machineRepository.destroy(id,status);
    }



    @Async
    public void startMachine(Long id,StatusEnum status1,StatusEnum status2) {

            Machine mch = machineRepository.findMachineByIdEquals(id);

            if(mch.getActive().equals(Boolean.FALSE)) { //PROVERA DA LI JE VEC ZAUZETA
                machineRepository.updateActiveTrue(id);
                if(mch.getStatus() != status1) {//PROVERA DA LI JE U STOP STANJU
                    try {
                        Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                        machineRepository.startAndStop(id, status1, status2);
                        machineRepository.updateActiveFalse(id);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                machineRepository.updateActiveFalse(id);

            }

    }



    @Async
    public void reservedStart(Long id,String res,StatusEnum status1,StatusEnum status2) {

        Machine mch = machineRepository.findMachineByIdEquals(id);
        String src[] = res.split("/");
        String src2[] = src[1].split("-");
        String fin = "0 " + src[2] + " " + src2[1] + " " + src[0] + " " + src2[0] + " *" ;

        if(mch.getActive().equals(Boolean.FALSE)) { //PROVERA DA LI JE VEC ZAUZETA
            if (mch.getStatus() != status1) {//PROVERA DA LI JE U STOP STANJU
                CronTrigger cronTrigger = new CronTrigger(fin);
                this.taskScheduler.schedule(() -> {
                    machineRepository.updateActiveTrue(id);
                    machineRepository.startAndStop(id, status1, status2);
                    machineRepository.updateActiveFalse(id);
                }, cronTrigger);
            }else {//AKO NIJE PRAVIMO GRESKU
                ErrorMessage err = new ErrorMessage();
                err.setMessage("Masina je u Running stanju");
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
                err.setMachineId(id);
                errorRepository.save(err);
            }
        }else {//AKO JESTE PRAVIMO GRESKU
            ErrorMessage err = new ErrorMessage();
            err.setMessage("Masina je vec zauzeta restart/stop operacijom");
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
            err.setMachineId(id);
            errorRepository.save(err);
        }
    }

    @Async
    public void stopMachine(Long id,StatusEnum status1,StatusEnum status2) {

        Machine mch = machineRepository.findMachineByIdEquals(id);
        if(mch.getActive().equals(Boolean.FALSE)) {//PROVERA DA LI JE VEC ZAUZETA
            machineRepository.updateActiveTrue(id);
            if(mch.getStatus() != status1) {// PROVERA DA LI JE U RUNNING STANJU
                try {
                    Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                    machineRepository.startAndStop(id, status1, status2);
                    machineRepository.updateActiveFalse(id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            machineRepository.updateActiveFalse(id);
        }

    }
    @Async
    public void reservedStop(Long id,String res,StatusEnum status1,StatusEnum status2) {

        Machine mch = machineRepository.findMachineByIdEquals(id);
        String src[] = res.split("/");
        String src2[] = src[1].split("-");
        String fin = "0 " + src[2] + " " + src2[1] + " " + src[0] + " " + src2[0] + " *" ;

        if(mch.getActive().equals(Boolean.FALSE)) { //PROVERA DA LI JE VEC ZAUZETA
            if (mch.getStatus() != status1) {//PROVERA DA LI JE U RUNNING STANJU
                CronTrigger cronTrigger = new CronTrigger(fin);
                this.taskScheduler.schedule(() -> {
                    machineRepository.updateActiveTrue(id);
                    machineRepository.startAndStop(id, status1, status2);
                    machineRepository.updateActiveFalse(id);
                }, cronTrigger);
            }else {//AKO NIJE PRAVIMO GRESKU
                ErrorMessage err = new ErrorMessage();
                err.setMessage("Masina je u Stop stanju");
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
                err.setMachineId(id);
                errorRepository.save(err);
            }
        }else {//AKO JESTE PRAVIMO GRESKU
            ErrorMessage err = new ErrorMessage();
            err.setMessage("Masina je vec zauzeta restart/start operacijom");
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
            err.setMachineId(id);
            errorRepository.save(err);
        }
    }


    @Async
    public void restartMachine(Long id,StatusEnum status1,StatusEnum status2) {

        Machine mch = machineRepository.findMachineByIdEquals(id);
        if(mch.getActive().equals(Boolean.FALSE)) {//PROVERA DA LI JE VEC ZAUZETA
            machineRepository.updateActiveTrue(id);
            if(mch.getStatus() != status2) {//PROVERA DA LI JE U RUNNING STANJU
                System.out.println("USAOOO");
                try {
                    Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                    machineRepository.startAndStop(id, status2, status1);
                    Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                    machineRepository.startAndStop(id, status1, status2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            machineRepository.updateActiveFalse(id);
        }

    }


    @Async
    public void reservedRestart(Long id,String res,StatusEnum status1,StatusEnum status2) {

        Machine mch = machineRepository.findMachineByIdEquals(id);
        String src[] = res.split("/");
        String src2[] = src[1].split("-");
        String fin = "0 " + src[2] + " " + src2[1] + " " + src[0] + " " + src2[0] + " *" ;

        if(mch.getActive().equals(Boolean.FALSE)) { //PROVERA DA LI JE VEC ZAUZETA
            if (mch.getStatus() != status2) {//PROVERA DA LI JE U RUNNING STANJU
                CronTrigger cronTrigger = new CronTrigger(fin); // "0 0 0 */25 * *"
                this.taskScheduler.schedule(() -> {
                    machineRepository.updateActiveTrue(id);
                    machineRepository.startAndStop(id, status2, status1);
                    try {
                        Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                        machineRepository.startAndStop(id, status1, status2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    machineRepository.updateActiveFalse(id);
                }, cronTrigger);
            }else {//AKO NIJE PRAVIMO GRESKU
                ErrorMessage err = new ErrorMessage();
                err.setMessage("Masina je u Stop stanju");
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
                err.setMachineId(id);
                errorRepository.save(err);
            }
        }else {//AKO JESTE PRAVIMO GRESKU
            ErrorMessage err = new ErrorMessage();
            err.setMessage("Masina je vec zauzeta start/stop operacijom");
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
            err.setMachineId(id);
            errorRepository.save(err);
        }
    }

    public List<Machine> findByName(String name,User2 id) {
       return  machineRepository.findMachinesByNameContainingAndCreatedByAndDestroyAndDestroyIsFalse(name,id,false);
    }


    public List<Machine> findByStatus(StatusEnum name,User2 id) {
        return  machineRepository.findMachinesByStatusAndCreatedByAndDestroyAndDestroyIsFalse(name,id,false);
    }

    public List<Machine> findByStatusAndName(StatusEnum status,String name,User2 id) {
        return  machineRepository.findMachinesByNameContainingAndStatusAndCreatedByAndDestroyAndDestroyIsFalse(name,status,id,false);
    }
    public List<Machine> findByNameAndDateFrom(String name,Date date,User2 id) {
        return  machineRepository.findMachinesByNameContainingAndDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(name,date,id,false);
    }
    public List<Machine> findByStatusAndDateFrom(StatusEnum status,Date date,User2 id) {
        return  machineRepository.findMachinesByStatusAndDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(status,date,id,false);
    }
    public List<Machine> findByStatusAndDateTo(StatusEnum status,Date date,User2 id) {
        return  machineRepository.findMachinesByStatusAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(status,date,id,false);
    }
    public List<Machine> findByDateFromAndDateTo(Date date1,Date date2,User2 id) {
        return  machineRepository.findMachinesByDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(date1,date2,id,false);
    }
    public List<Machine> findByStatusAndNameAndDateFrom(StatusEnum status,String name,Date date,User2 id) {
        return  machineRepository.findMachinesByStatusAndNameContainingAndDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(status,name,date,id,false);
    }
    public List<Machine> findByStatusAndNameAndDateTo(StatusEnum status,String name,Date date,User2 id) {
        return  machineRepository.findMachinesByStatusAndNameContainingAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(status,name,date,id,false);
    }
    public List<Machine> findByStatusAndNameAndDateFromAndDateTo(StatusEnum status,String name,Date date1,Date date2,User2 id) {
        return  machineRepository.findMachinesByStatusAndNameContainingAndDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(status,name,date1,date2,id,false);
    }

    public List<Machine> findByDateFrom(Date date,User2 id) {
        return  machineRepository.findMachinesByDateGreaterThanAndCreatedByAndDestroyAndDestroyIsFalse(date,id,false);
    }
    public List<Machine> findByDateTo(Date date,User2 id) {
        return  machineRepository.findMachinesByDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(date,id,false);
    }

    public List<Machine> findByNameAndDateTo(String name,Date date,User2 id) {
        return  machineRepository.findMachinesByNameContainingAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(name,date,id,false);
    }

    public List<Machine> findByNameAndDateFromAndDateTo(String name,Date date1, Date date2,User2 id) {
        return  machineRepository.findMachinesByNameContainingAndDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(name,date1,date2,id,false);
    }
    public List<Machine> findByStatusAndDateFromAndDateTo(StatusEnum status, Date date1, Date date2,User2 id) {
        return  machineRepository.findMachinesByStatusAndDateGreaterThanAndDateLessThanAndCreatedByAndDestroyAndDestroyIsFalse(status,date1,date2,id,false);
    }

    public ErrorMessage saveError(ErrorMessage err) {
        return errorRepository.save(err);
    }

}
