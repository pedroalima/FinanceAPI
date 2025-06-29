package com.test.finance_api.services;

import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionRequestDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.UserNotFoundException;
import com.test.finance_api.infra.mapper.TransactionMapper;
import com.test.finance_api.repositories.TransactionRepository;
import com.test.finance_api.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

    public ResponseEntity<TransactionResponseDTO> create(@RequestBody TransactionRequestDTO body) {
        this._userRepository.findById(body.userId()).orElseThrow(() -> new UserNotFoundException("UserId: " + body.userId() + " não encontrado"));

        Transaction newTransaction = this._transactionMapper.transactionRequestDTOToTransaction(body);

        if (newTransaction.getDescription().isBlank() || newTransaction.getDescription() == null) {
            newTransaction.setDescription("Sem descrição");
        }

        this._transactionRepository.save(newTransaction);

        TransactionDTO transactionDTO = this._transactionMapper.transactionToTransactionDTO(newTransaction);

        return ResponseEntity.ok(new TransactionResponseDTO("Transação criada com sucesso!", transactionDTO));
    }
}
