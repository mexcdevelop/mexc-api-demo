import unittest
from mexc_bot import Config, DonchianSignal, RiskManager


def kline(high, low, close, ts=1):
    return [ts, 0, high, low, close, 0]


class SignalTests(unittest.TestCase):
    def test_buy_breakout(self):
        s = DonchianSignal(3, 2)
        data = [kline(10, 8, 9), kline(11, 9, 10), kline(12, 10, 11), kline(14, 12, 13)]
        self.assertEqual(s.evaluate(data, False), "BUY")

    def test_sell_breakdown(self):
        s = DonchianSignal(3, 2)
        data = [kline(10, 8, 9), kline(11, 9, 10), kline(12, 10, 11), kline(9, 7, 7.5)]
        self.assertEqual(s.evaluate(data, True), "SELL")


class RiskTests(unittest.TestCase):
    def test_position_sizing(self):
        cfg = Config(max_position_pct=5, min_order_notional=10)
        r = RiskManager(cfg)
        self.assertAlmostEqual(r.order_quantity(1000, 100), 0.5)

    def test_daily_loss_kill_switch(self):
        cfg = Config(max_daily_loss_pct=2)
        r = RiskManager(cfg)
        r.check_daily_loss(1000)
        with self.assertRaises(RuntimeError):
            r.check_daily_loss(970)


if __name__ == "__main__":
    unittest.main()
