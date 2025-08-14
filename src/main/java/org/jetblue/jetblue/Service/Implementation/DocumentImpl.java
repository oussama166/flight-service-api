package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Repositories.DocumentRepo;
import org.jetblue.jetblue.Service.DocumentService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentImpl implements DocumentService {
    private DocumentRepo documentRepo;
}
