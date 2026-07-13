package com.inv.invmaster001.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class NumberToWordsUtilTest {

    @Test
    void zeroAmount() {
        assertThat(NumberToWordsUtil.convert(BigDecimal.ZERO))
                .isEqualTo("Zero Rupees Only");
    }

    @Test
    void singleRupeeIsSingular() {
        assertThat(NumberToWordsUtil.convert(new BigDecimal("1")))
                .isEqualTo("One Rupee Only");
    }

    @Test
    void singlePaisaIsSingular() {
        assertThat(NumberToWordsUtil.convert(new BigDecimal("0.01")))
                .isEqualTo("Zero Rupees and One Paisa Only");
    }

    @Test
    void rupeesAndPaise() {
        assertThat(NumberToWordsUtil.convert(new BigDecimal("236.50")))
                .isEqualTo("Two Hundred Thirty Six Rupees and Fifty Paise Only");
    }

    @Test
    void lakhPath() {
        assertThat(NumberToWordsUtil.convert(new BigDecimal("1234567.89")))
                .isEqualTo("Twelve Lakh Thirty Four Thousand Five Hundred Sixty Seven Rupees"
                        + " and Eighty Nine Paise Only");
    }

    @Test
    void crorePath() {
        assertThat(NumberToWordsUtil.convert(new BigDecimal("10000000")))
                .isEqualTo("One Crore Rupees Only");
    }

    @Test
    void teensAndTensBoundaries() {
        assertThat(NumberToWordsUtil.convert(new BigDecimal("19")))
                .isEqualTo("Nineteen Rupees Only");
        assertThat(NumberToWordsUtil.convert(new BigDecimal("20")))
                .isEqualTo("Twenty Rupees Only");
        assertThat(NumberToWordsUtil.convert(new BigDecimal("21")))
                .isEqualTo("Twenty One Rupees Only");
    }
}
