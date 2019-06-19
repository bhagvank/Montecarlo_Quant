import IPython
import numpy as np
import warnings
warnings.filterwarnings("ignore")
from sys import version

class LeastSquareMontecarlo(object):

    def __init__(self, option_type, S0, strike, T, M, r, div, sigma, simulations):
        try:
            self.option_type = option_type
            assert isinstance(option_type, str)
            self.S0 = float(S0)
            self.strike = float(strike)
            assert T > 0
            self.T = float(T)
            assert M > 0
            self.M = int(M)
            assert r >= 0
            self.r = float(r)
            assert div >= 0
            self.div = float(div)
            assert sigma > 0
            self.sigma = float(sigma)
            assert simulations > 0
            self.simulations = int(simulations)
        except ValueError:
            print('Error passing Options parameters')


        if option_type != 'call' and option_type != 'put':
            raise ValueError("Error: option type not valid. Enter 'call' or 'put'")
        if S0 < 0 or strike < 0 or T <= 0 or r < 0 or div < 0 or sigma < 0:
            raise ValueError('Error: Negative inputs not allowed')

        self.time_unit = self.T / float(self.M)
        self.discount = np.exp(-self.r * self.time_unit)

    @property
    def MCprice_matrix(self, seed = 123):
        """ Returns MC price matrix rows: time columns: price-path simulation """
        np.random.seed(seed)
        MCprice_matrix = np.zeros((self.M + 1, self.simulations), dtype=np.float64)
        MCprice_matrix[0,:] = self.S0
        for t in range(1, self.M + 1):
            brownian = np.random.standard_normal(int( self.simulations / 2))
            brownian = np.concatenate((brownian, -brownian))
            MCprice_matrix[t, :] = (MCprice_matrix[t - 1, :]
                                  * np.exp((self.r - self.sigma ** 2 / 2.) * self.time_unit
                                  + self.sigma * brownian * np.sqrt(self.time_unit)))
        return MCprice_matrix

    @property
    def MCpayoff(self):
        """Returns the inner-value of American Option"""
        if self.option_type == 'call':
            payoff = np.maximum(self.MCprice_matrix - self.strike,
                           np.zeros((self.M + 1, self.simulations),dtype=np.float64))
        else:
            payoff = np.maximum(self.strike - self.MCprice_matrix,
                            np.zeros((self.M + 1, self.simulations),
                            dtype=np.float64))
        return payoff

    @property
    def value_vector(self):
        value_matrix = np.zeros_like(self.MCpayoff)
        value_matrix[-1, :] = self.MCpayoff[-1, :]
        for t in range(self.M - 1, 0 , -1):
            regression = np.polyfit(self.MCprice_matrix[t, :], value_matrix[t + 1, :] * self.discount, 5)
            continuation_value = np.polyval(regression, self.MCprice_matrix[t, :])
            value_matrix[t, :] = np.where(self.MCpayoff[t, :] > continuation_value,
                                          self.MCpayoff[t, :],
                                          value_matrix[t + 1, :] * self.discount)

        return value_matrix[1,:] * self.discount


    @property
    def price(self): return np.sum(self.value_vector) / float(self.simulations)

    @property
    def delta(self):
        diff = self.S0 * 0.01
        myCall_1 = LeastSquareMontecarlo(self.option_type, self.S0 + diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        myCall_2 = LeastSquareMontecarlo(self.option_type, self.S0 - diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        return (myCall_1.price - myCall_2.price) / float(2. * diff)

    @property
    def gamma(self):
        diff = self.S0 * 0.01
        myCall_1 = LeastSquareMontecarlo(self.option_type, self.S0 + diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        myCall_2 = LeastSquareMontecarlo(self.option_type, self.S0 - diff,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma, self.simulations)
        return (myCall_1.delta - myCall_2.delta) / float(2. * diff)

    @property
    def vega(self):
        diff = self.sigma * 0.01
        myCall_1 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma + diff,
                                       self.simulations)
        myCall_2 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma - diff,
                                       self.simulations)
        return (myCall_1.price - myCall_2.price) / float(2. * diff)

    @property
    def rho(self):
        diff = self.r * 0.01
        if (self.r - diff) < 0:
            myCall_1 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r + diff, self.div, self.sigma,
                                       self.simulations)
            myCall_2 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r, self.div, self.sigma,
                                       self.simulations)
            return (myCall_1.price - myCall_2.price) / float(diff)
        else:
            myCall_1 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r + diff, self.div, self.sigma,
                                       self.simulations)
            myCall_2 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T, self.M,
                                       self.r - diff, self.div, self.sigma,
                                       self.simulations)
            return (myCall_1.price - myCall_2.price) / float(2. * diff)

    @property
    def theta(self):
        diff = 1 / 252.
        myCall_1 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T + diff, self.M,
                                       self.r, self.div, self.sigma,
                                       self.simulations)
        myCall_2 = LeastSquareMontecarlo(self.option_type, self.S0,
                                       self.strike, self.T - diff, self.M,
                                       self.r, self.div, self.sigma,
                                       self.simulations)
        return (myCall_2.price - myCall_1.price) / float(2. * diff)
