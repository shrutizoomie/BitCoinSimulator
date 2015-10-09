/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sjsu.thesis.backend;

import java.io.IOException;

public class BitFedGenesisBlock
{
	private BitFedBlockChain bc;
	private BitFedBlock b;
	private BitFedTransaction t;

	public BitFedGenesisBlock() throws IOException
	{
		this.bc = new BitFedBlockChain();
		this.b = new BitFedBlock();
		String pub = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEjRod7Cu2XtTn+sZMOuM9gELSaLMJuXKXyUokJCJSoiUiuhAj7yTPTlvBitSn/zbM+/1crjmqlMklBCN+0CPjsQ==";
		this.t = new BitFedTransaction(pub, 0);
		b.addTransaction(t);
		bc.addBlock(b);
	}

	public BitFedBlockChain getGenesisBlock()
	{
		return bc;
	}
}

