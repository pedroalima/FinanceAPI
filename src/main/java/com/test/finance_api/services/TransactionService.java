package com.test.finance_api.services;

import com.test.finance_api.dto.transaction.CreateTransactionRequestDTO;
import com.test.finance_api.dto.transaction.TransactionDTO;
import com.test.finance_api.dto.transaction.TransactionResponseDTO;
import com.test.finance_api.entity.Transaction;
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
    private final TransactionRepository _repository;
    private final TransactionMapper _mapper;
    private final UserValidator _userValidator;
    private final TransactionValidator _transactionValidator;

    public TransactionService(
            TransactionRepository transactionRepository,
            TransactionMapper transactionMapper,
            UserValidator userValidator,
            TransactionValidator transactionValidator
    ) {
        this._repository = transactionRepository;
        this._mapper = transactionMapper;
        this._userValidator = userValidator;
        this._transactionValidator = transactionValidator;
    }

    public ResponseEntity<TransactionResponseDTO> getAllByUserId(
            @PathVariable(value = "userId") String userId
    ) {
        this._userValidator.assertByUserId(userId);

        List<Transaction> transactionList = this._repository.findAllByUserId(userId);

        TransactionDTO[] transactionListDTO = this._mapper.transactionListToDTO(transactionList);

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
        this._userValidator.assertByUserId(userId);

        Transaction transaction = this._transactionValidator.assertById(transactionId);

        TransactionDTO transactionDTO = this._mapper.transactionToDTO(transaction);

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
        this._userValidator.assertByUserId(userId);

        Transaction newTransaction = this._mapper.DTOToTransaction(body);

        if (newTransaction.getDescription() == null || newTransaction.getDescription().isBlank()) {
            newTransaction.setDescription("Sem descrição");
        }
        newTransaction.setUserId(userId);

        this._repository.save(newTransaction);

        TransactionDTO transactionDTO = this._mapper.transactionToDTO(newTransaction);

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
        this._userValidator.assertByUserId(userId);

        Transaction existingTransaction = this._transactionValidator.assertById(transactionId);

        existingTransaction.setAmount(body.amount());
        existingTransaction.setDate(body.date());
        existingTransaction.setDescription(body.description());
        existingTransaction.setCategory(body.category());
        existingTransaction.setAccount(body.account());
        existingTransaction.setType(body.type());
        existingTransaction.setInstallment(body.installment());
        existingTransaction.setInstallmentValue(body.installmentValue());

        this._repository.save(existingTransaction);

        TransactionDTO transactionDTO = this._mapper.transactionToDTO(existingTransaction);

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação atualizada com sucesso!",
                transactionDTO,
                null
        ));
    }

    public ResponseEntity<TransactionResponseDTO> delete(
            @PathVariable(value = "userId") String userId,
            @PathVariable(value = "transactionId") String transactionId
    ) {
        this._userValidator.assertByUserId(userId);
        Transaction existingTransaction = this._transactionValidator.assertById(transactionId);
        this._transactionValidator.assertTransactionBelongsToUser(userId, transactionId, existingTransaction);

        this._repository.delete(existingTransaction);

        TransactionDTO[] updatedTransactions = this._mapper.transactionListToDTO(
                this._repository.findAllByUserId(userId)
        );

        return ResponseEntity.ok(new TransactionResponseDTO(
                "Transação deletada com sucesso!",
                null,
                updatedTransactions
        ));
    }
}
