package com.desafiospring.desafio.services;

import com.desafiospring.desafio.dtos.ClienteDTO;
import com.desafiospring.desafio.dtos.ClienteDataDTO;
import com.desafiospring.desafio.dtos.StatusDTO;
import com.desafiospring.desafio.exceptions.ClientException;
import com.desafiospring.desafio.repositories.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientesServicioImple implements ClientesServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    // método que crea el cliente nuevo
    private ClienteDataDTO crearCliente(ClienteDTO data, String pass) throws ClientException {
        ClienteDataDTO nuevoCliente = new ClienteDataDTO();
        nuevoCliente.setDni(data.getDni());
        nuevoCliente.setNombre(data.getNombre());
        nuevoCliente.setEmail(data.getEmail());
        nuevoCliente.setProvincia(data.getProvincia());
        nuevoCliente.setPassword(pass);
        clienteRepositorio.guardarCliente(nuevoCliente);
        return nuevoCliente;
    }

    // método para validar las credenciales del cliente (previo a crearlo)
    private void validarCredenciales(ClienteDTO data, String pass) throws ClientException {
        Map<Integer, ClienteDataDTO> repositorio = clienteRepositorio.cargarClientes();
        if (repositorio.containsKey(data.getDni())) {
            throw new ClientException("El cliente ya se encuentra registrado");
        } else if (data.getDni() < 0) {
            throw new ClientException("El dni es inválido");
        }
        if (!validarEmail(data.getEmail())) {
            throw new ClientException("El email es inválido");
        }
        if (!validarPassword(pass)) {
            throw new ClientException("La contraseña es inválida");
        }
        if (data.getProvincia().isEmpty()) {
            throw new ClientException("No se ha indicado la provincia del nuevo cliente");
        }
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern p = Pattern.compile(regex);
        if (email.isEmpty()) {
            return false;
        }
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean validarPassword(String pass) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,12}$"; // entre 6 y 12 caracteres, nummeros, mayusculas y minusculas
        Pattern p = Pattern.compile(regex);
        if (pass.isEmpty()) {
            return false;
        }
        Matcher m = p.matcher(pass);
        return m.matches();
    }

    // método que da el alta (valida credenciales, guardo el cliente en memoria y devuelve un mensaje exitoso)
    @Override
    public StatusDTO darDeAlta(ClienteDTO data, String pass) throws ClientException {
        Map<Integer, ClienteDataDTO> repositorio = clienteRepositorio.cargarClientes();
        validarCredenciales(data, pass);
        ClienteDataDTO cliente = crearCliente(data, pass);
        return new StatusDTO(200, "El cliente " + data.getNombre() + " fue satisfactoriamente creado.");
    }

    // método que lista todos los clientes registrados (admite filtro por provincia)
    @Override
    public List<ClienteDTO> listarClientes(String provincia) throws ClientException {
        List<ClienteDTO> lista = new ArrayList<>();
        Map<Integer, ClienteDataDTO> repositorio = clienteRepositorio.cargarClientes();
        for (Map.Entry<Integer, ClienteDataDTO> entry : repositorio.entrySet()) {
            ClienteDTO cliente = new ClienteDTO();
            cliente.setDni(entry.getKey());
            cliente.setNombre(entry.getValue().getNombre());
            cliente.setEmail(entry.getValue().getEmail());
            cliente.setProvincia(entry.getValue().getProvincia());
            lista.add(cliente);
        }
        if (!provincia.isEmpty()) {
            lista.removeIf(c -> !c.getProvincia().equalsIgnoreCase(provincia));
        }
        if (lista.isEmpty()) {
            throw new ClientException("No hay clientes de la provincia " + provincia);
        }
        return lista;
    }
}
