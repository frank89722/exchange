package me.frankv.core.transaction;

public class TradingPairNotReadyException extends RuntimeException {
    public TradingPairNotReadyException(String tradingPairName) {
        super(tradingPairName + " is not ready to use yet");
    }
}
