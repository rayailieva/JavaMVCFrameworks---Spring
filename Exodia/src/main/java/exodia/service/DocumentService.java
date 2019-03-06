package exodia.service;

import exodia.domain.models.service.DocumentServiceModel;

import java.util.List;

public interface DocumentService {

    DocumentServiceModel createDocument(DocumentServiceModel documentServiceModel);

    DocumentServiceModel findDocumentById(String id);

    List<DocumentServiceModel> findAllDocuments();

    boolean printDocument(String id);
}
