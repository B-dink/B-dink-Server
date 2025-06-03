package com.app.bdink.payment.apple;

import com.app.bdink.member.entity.Member;

public interface ApplePurchaseService {

    PurchaseResponse purchase(PurchaseRequest request);
}
