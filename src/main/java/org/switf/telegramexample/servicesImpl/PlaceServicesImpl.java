package org.switf.telegramexample.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.switf.telegramexample.models.PlaceModel;
import org.switf.telegramexample.repositories.PlaceJpaRepository;
import org.switf.telegramexample.services.PlaceService;

@Service
public class PlaceServicesImpl implements PlaceService {
    @Autowired
    private PlaceJpaRepository repository;

    @Override
    public PlaceModel createPlace(PlaceModel request){
        PlaceModel placeModel = new PlaceModel();
        placeModel.setId(null);
        placeModel.setNombre(request.getNombre());
        placeModel.setDescripcion(request.getDescripcion());
        placeModel.setDireccion(request.getDireccion());

        return repository.save(placeModel);
    }
}
