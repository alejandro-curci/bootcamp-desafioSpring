package com.desafiospring.desafio.repositories;


import com.desafiospring.desafio.dtos.ClienteDataDTO;
import com.desafiospring.desafio.exceptions.ClientException;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ClienteRepositorioImple implements ClienteRepositorio {

    // método que recibe el ClienteDataDTO y lo escribe en el archivo csv para persistir la informacion
    @Override
    public void guardarCliente(ClienteDataDTO cliente) throws ClientException {
        BufferedWriter bw = null;
        FileWriter fw = null;
        BufferedReader br = null;
        FileReader fr = null;
        try {
            br = new BufferedReader(new FileReader("src/main/resources/dbClientes.csv"));
            bw = new BufferedWriter(new FileWriter("src/main/resources/dbClientes.csv", true));
            String linea = br.readLine();
            while (linea != null) {
                linea = br.readLine();
            }
            bw.write(System.lineSeparator() + cliente.getDni() + "," + cliente.getNombre() + "," + cliente.getEmail() + "," + cliente.getPassword() + "," + cliente.getProvincia());
            bw.flush();
        } catch (IOException e) {
            throw new ClientException("Error al guardar el cliente");
        } finally {
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    // método que lea el repositorio existente (y lo traslado al hashmap en memoria)
    @Override
    public Map<Integer, ClienteDataDTO> cargarClientes() {
        BufferedReader bufferLectura = null;
        Map<Integer, ClienteDataDTO> clientes = new HashMap<>();
        int fila = 0;
        try {
            bufferLectura = new BufferedReader(new FileReader("src/main/resources/dbClientes.csv"));
            String linea = bufferLectura.readLine();
            while (linea != null) {
                String[] celdas = linea.split(",");
                // salteo la primer fila (títulos)
                if (fila > 0) {
                    ClienteDataDTO clienteDataDTO = new ClienteDataDTO();
                    clienteDataDTO.setDni(Integer.parseInt(celdas[0]));
                    clienteDataDTO.setNombre(celdas[1]);
                    clienteDataDTO.setEmail(celdas[2]);
                    clienteDataDTO.setPassword(celdas[3]);
                    clienteDataDTO.setProvincia(celdas[4]);
                    clientes.put(clienteDataDTO.getDni(), clienteDataDTO);
                }
                linea = bufferLectura.readLine();
                fila++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferLectura != null) {
                try {
                    bufferLectura.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return clientes;
    }


}
