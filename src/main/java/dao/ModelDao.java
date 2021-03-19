package dao;

import model.Model;
import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;

public interface ModelDao {

    void save(Model model) throws StoreException;

    Model getByDimension(FieldDimension fieldDimension) throws FetchException;

}
