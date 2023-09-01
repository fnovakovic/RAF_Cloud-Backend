package rs.raf.demo.services;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.*;

import rs.raf.demo.repositories.ErrorRepository;

import java.util.List;

@Service
public class ErrorService {

    private ErrorRepository errorRepository;

    public ErrorService(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }


    public List<ErrorMessage> find(Long id) {
        return errorRepository.findAll();
    }

}
