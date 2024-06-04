package com.habsida.morago.controllers;

import com.habsida.morago.dtos.DepositsInput;
import com.habsida.morago.model.entity.Deposits;
import com.habsida.morago.resolver.DepositsResolver;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DepositsController {

    private  final DepositsResolver depositsResolver;

    public DepositsController(DepositsResolver depositsResolver){
        this.depositsResolver = depositsResolver;
    }

    @QueryMapping
    public List<Deposits> getAllDeposits() throws Exception{
        return depositsResolver.getAllDeposits();
    }

    @QueryMapping
    public Deposits getDepositById(@Argument Long id) throws Exception {
        return depositsResolver.getDepositById(id);
    }

    @MutationMapping
    public Deposits addDeposit(@Argument DepositsInput depositsDto) {
        return depositsResolver.addDeposit(depositsDto);
    }

    @MutationMapping
    public Deposits updateDeposit(@Argument Long id, @Argument DepositsInput depositsDto) throws Exception{
        return depositsResolver.updateDeposit(id,depositsDto);
    }

    @MutationMapping
    public Boolean deleteDeposit(@Argument Long id) throws Exception{
        return depositsResolver.deleteDeposit(id);
    }

}