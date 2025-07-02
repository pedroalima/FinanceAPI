package com.test.finance_api.services;

import com.test.finance_api.dto.transaction.*;
import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.UserNotFoundException;
import com.test.finance_api.infra.mapper.TransactionMapper;
import com.test.finance_api.repositories.TransactionRepository;
import com.test.finance_api.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository _transactionRepository;
    private final UserRepository _userRepository;
    private final TransactionMapper _transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, TransactionMapper transactionMapper) {
        this._transactionRepository = transactionRepository;
        this._userRepository = userRepository;
        this._transactionMapper = transactionMapper;
    }

    public ResponseEntity<TransactionResponseDTO> getAllByUserId(@PathVariable(value = "userId") String userId) {
        this._userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("UserId: " + userId + " não encontrado"));

        List<Transaction> transactionList = this._transactionRepository.findAllByUserId(userId);

        TransactionDTO[] transactionListDTO = this._transactionMapper.transactionListToTransactionListDTO(transactionList);

        if (transactionListDTO.length == 0) {
            return ResponseEntity.ok(new TransactionResponseDTO(
                    "Nenhuma transação encontrada para o usuário",
                    null,
                    new TransactionDTO[0]
            ));
        }

        return ResponseEntity.ok(new TransactionResponseDTO(
                        "Todas as transações do usuário",
                        null,
                        transactionListDTO
        ));
    }

    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable(value = "userId") String userId,
            @PathVariable(value = "transactionId") String transactionId
    ) {
        this._userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("UserId: " + userId + " não encontrado"));

        Transaction transaction = this._transactionRepository.findById(transactionId)
                .orElseThrow(() -> new UserNotFoundException("TransactionId: " + transactionId + " não encontrado"));

        TransactionDTO transactionDTO = this._transactionMapper.transactionToTransactionDTO(transaction);

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação encontrada com sucesso!",
                transactionDTO,
                null
        ));
    }

    public ResponseEntity<TransactionResponseDTO> create(@RequestBody CreateTransactionRequestDTO body) {
        this._userRepository.findById(body.userId())
                .orElseThrow(() -> new UserNotFoundException("UserId: " + body.userId() + " não encontrado"));

        Transaction newTransaction = this._transactionMapper.transactionRequestDTOToTransaction(body);

        if (newTransaction.getDescription().isBlank() || newTransaction.getDescription() == null) {
            newTransaction.setDescription("Sem descrição");
        }

        this._transactionRepository.save(newTransaction);

        TransactionDTO transactionDTO = this._transactionMapper.transactionToTransactionDTO(newTransaction);

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação criada com sucesso!",
                transactionDTO,
                null
        ));
    }
}
