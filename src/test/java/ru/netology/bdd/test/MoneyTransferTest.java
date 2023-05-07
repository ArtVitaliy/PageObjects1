package ru.netology.bdd.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.page.DashboardPage;
import ru.netology.bdd.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.bdd.data.DataHelper.*;


public class MoneyTransferTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferFromFirstToSecond() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = generateValidAmount(dashboardPage.getCardBalance(0));
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(0);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(1);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    public void shouldTransferFromSecondToFirst() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = generateValidAmount(dashboardPage.getCardBalance(1));
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(0);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(1);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    public void shouldTransferFromFirstToSecondOverLimit() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = generateInvalidAmount(dashboardPage.getCardBalance(0));
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(0);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(1);

        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }
}
