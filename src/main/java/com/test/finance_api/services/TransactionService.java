package com.test.finance_api.services;

import com.test.finance_api.dto.transaction.CreateTransactionRequestDTO;
import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.entity.Transaction;
import com.test.finance_api.exceptions.TransactionNotFoundException;
import com.test.finance_api.infra.mapper.TransactionMapper;
import com.test.finance_api.repositories.TransactionRepository;
import com.test.finance_api.validators.TransactionValidator;
import com.test.finance_api.validators.UserValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository _transactionRepository;
    private final TransactionMapper _transactionMapper;
    private final UserValidator _userValidator;
    private final TransactionValidator _transactionValidator;

    public TransactionService(
            TransactionRepository transactionRepository,
            TransactionMapper transactionMapper,
            UserValidator userValidator,
            TransactionValidator transactionValidator
    ) {
        this._transactionRepository = transactionRepository;
        this._transactionMapper = transactionMapper;
        this._userValidator = userValidator;
        this._transactionValidator = transactionValidator;
    }

    public ResponseEntity<TransactionResponseDTO> getAllByUserId(
            @PathVariable(value = "userId") String userId
    ) {
        this._userValidator.verify(userId);

        List<Transaction> transactionList = this._transactionRepository.findAllByUserId(userId);

        TransactionDTO[] transactionListDTO = this._transactionMapper.transactionListToDTO(transactionList);

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
        this._userValidator.verify(userId);

        Transaction transaction = this._transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("TransactionId: " + transactionId + " não encontrado"));

        TransactionDTO transactionDTO = this._transactionMapper.transactionToDTO(transaction);

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação encontrada com sucesso!",
                transactionDTO,
                null
        ));
    }

    public ResponseEntity<TransactionResponseDTO> create(
            @PathVariable(value = "userId") String userId,
            @RequestBody CreateTransactionRequestDTO body
    ) {
        this._userValidator.verify(userId);

        Transaction newTransaction = this._transactionMapper.DTOToTransaction(body);

        newTransaction.setUserId(userId);

        if (newTransaction.getDescription().isBlank() || newTransaction.getDescription() == null) {

            newTransaction.setDescription("Sem descrição");
        }

        this._transactionRepository.save(newTransaction);

        TransactionDTO transactionDTO = this._transactionMapper.transactionToDTO(newTransaction);

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação criada com sucesso!",
                transactionDTO,
                null
        ));
    }

    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable(value = "userId") String userId,
            @PathVariable(value = "transactionId") String transactionId,
            @RequestBody CreateTransactionRequestDTO body
    ) {
        this._userValidator.verify(userId);

        Transaction existingTransaction = this._transactionValidator.verifyAndReturn(transactionId);

        existingTransaction.setAmount(body.amount());
        existingTransaction.setDate(body.date());
        existingTransaction.setDescription(body.description());
        existingTransaction.setCategory(body.category());
        existingTransaction.setAccount(body.account());
        existingTransaction.setType(body.type());
        existingTransaction.setInstallment(body.installment());
        existingTransaction.setInstallmentValue(body.installmentValue());

        this._transactionRepository.save(existingTransaction);

        TransactionDTO transactionDTO = this._transactionMapper.transactionToDTO(existingTransaction);

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação atualizada com sucesso!",
                transactionDTO,
                null
        ));
    }
}
