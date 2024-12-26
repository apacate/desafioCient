package com.apacate.client.services;

import com.apacate.client.dto.ClientDTO;
import com.apacate.client.entities.Client;
import com.apacate.client.repositories.ClientRepository;
import com.apacate.client.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Metodo para retornar todos os clientes com paginação
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(ClientDTO::new);
    }

    // Metodo para buscar um cliente por ID
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
        return new ClientDTO(client);
    }

    // Metodo para inserir um novo cliente
    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        Client entity = new Client();
        entity = clientRepository.save(entity);
        return new ClientDTO(entity);
    }


    // Metodo para atualizar um cliente existente
    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        try {
            Client client = clientRepository.getReferenceById(id);
            updateClientData(client, clientDTO);
            client = clientRepository.save(client);
            return new ClientDTO(client);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Cliente não encontrado para atualização: " + id);
        }
    }

    // Metodo auxiliar para atualizar os dados do cliente
    private void updateClientData(Client client, ClientDTO clientDTO) {
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setIncome(clientDTO.getIncome());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setChildren(clientDTO.getChildren());
    }

    // Metodo para deletar um cliente por ID
    @Transactional
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado para exclusão: " + id);
        }
        clientRepository.deleteById(id);
    }

    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }

}
