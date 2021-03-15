package main;

import entity.Field;
import entity.Model;
import enums.FieldDimension;
import util.CellGenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Path p = Paths.get("A:\\field.txt");
        if(!Files.exists(p)){
            Files.createFile(p);
        }
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(p.toFile())));
        Field field = Field.fromDimension(FieldDimension.FOUR_AND_FOUR);
        oos.writeObject(field);
        oos.flush();
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(p.toFile())));
        Field f = (Field) ois.readObject();
        System.out.println(f);

    }

}
