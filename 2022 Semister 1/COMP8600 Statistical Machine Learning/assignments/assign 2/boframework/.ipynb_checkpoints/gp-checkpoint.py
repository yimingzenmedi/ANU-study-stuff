from __future__ import annotations
from boframework.kernels import Matern
from scipy.linalg import cho_solve, cholesky, solve_triangular
from scipy.optimize import minimize
from typing import Callable, Tuple, Union, Type
import copy
from operator import itemgetter
import numpy as np

# Class Structure


class GPRegressor:
    """
    Gaussian process regression (GPR).

    Arguments:
    ----------
    kernel : kernel instance,
        The kernel specifying the covariance function of the GP.

    noise_level : float , default=1e-10
        Value added to the diagonal of the kernel matrix during fitting.
        It can be interpreted as the variance of additional Gaussian
        measurement noise on the training observations.

    n_restarts : int, default=0
        The number of restarts of the optimizer for finding the kernel's
        parameters which maximize the log-marginal likelihood. The first run
        of the optimizer is performed from the kernel's initial parameters,
        the remaining ones (if any) from thetas sampled log-uniform randomly
        (for more details: https://en.wikipedia.org/wiki/Reciprocal_distribution)
        from the space of allowed theta-values. If greater than 0, all bounds
        must be finite. Note that `n_restarts == 0` implies that one
        run is performed.

    random_state : RandomState instance
    """

    def __init__(self,
                 kernel: Matern,
                 noise_level: float = 1e-10,
                 n_restarts: int = 0,
                 random_state: Type[np.random.RandomState] = np.random.RandomState
                 ) -> None:

        self.kernel = kernel
        self.noise_level = noise_level
        self.n_restarts = n_restarts
        self.random_state = random_state(4)

    def optimisation(self,
                     obj_func: Callable,
                     initial_theta: np.ndarray,
                     bounds: Tuple
                     ) -> Tuple[np.ndarray, np.ndarray]:
        """
        Function that performs Quasi-Newton optimisation using L-BFGS-B algorithm.

        Note that we should frame the problem as a minimisation despite trying to
        maximise the log marginal likelihood.

        Arguments:
        ----------
        obj_func : the function to optimise as a callable
        initial_theta : the initial theta parameters, use under x0
        bounds : the bounds of the optimisation search

        Returns:
        --------
        theta_opt : the best solution x*
        func_min : the value at the best solution x, i.e, p*
        """
        # TODO Q2.2
        # Implement an L-BFGS-B optimisation algorithm using scipy.minimize built-in function

        # FIXME
        
        res = minimize(obj_func, initial_theta, bounds=bounds)
#         print("!!!!!!! optimisation res:", res)
        return res.x, np.array(res.fun)

        raise NotImplementedError

    def fit(self, X: np.ndarray, y: np.ndarray) -> GPRegressor:
        """
        Fit Gaussian process regression model.

        Arguments:
        ----------
        X : ndarray of shape (n_samples, n_features)
            Feature vectors or other representations of training data.
        y : ndarray of shape (n_samples, n_targets)
            Target values.

        Returns:
        --------
        self : object
            The current GPRegressor class instance.
        """
        # TODO Q2.2
        # Fit the Gaussian process by performing hyper-parameter optimisation
        # using the log marginal likelihood solution. To maximise the marginal
        # likelihood, you should use the `optimisation` function

        # HINT I: You should run the optimisation (n_restarts) time for optimum results.

        # HINT II: We have given you a data structure for all hyper-parameters under the variable `theta`,
        #           coming from the Matern class. You can assume by optimising `theta` you are optimising
        #           all the hyper-parameters.

        # HINT III: Implementation detail - Note that theta contains the log-transformed hyperparameters
        #               of the kernel, so now we are operating on a log-space. So your sampling distribution
        #               should be uniform.

        self._kernel = copy.deepcopy(self.kernel)

        self._X_train = X
        self._y_train = y

        # FIXME
        
#         res_list = [] 
        bounds = self.kernel.bounds
        theta_opt, func_min = self.optimisation(self.minurs_log_marginal_likelihood, self.kernel.theta, bounds)
#         print("theta_opt:", theta_opt)
#         print("func_min:", func_min)
#         res_list.append([theta_opt, func_min])
        res_theta = theta_opt
        res_func_min = func_min
        for i in range(1, self.n_restarts):
            init_theta = self.kernel.theta  # ?
            theta_opt, func_min = self.optimisation(self.minurs_log_marginal_likelihood, init_theta, bounds)
            if func_min < res_func_min:
                res_theta = theta_opt
                res_func_min = func_min
#             res_list.append([theta_opt, func_min])

#         res_list = np.array(res_list)
#         print("res_list:", res_list)

        self.kernel.theta = res_theta
        
        return self
    
        raise NotImplementedError
        
    def minurs_log_marginal_likelihood(self, theta):
        return 0 - self.log_marginal_likelihood(theta)

    def predict(self, X: np.ndarray, return_std: bool = False) -> Union[np.ndarray, Tuple[np.ndarray, np.ndarray]]:
        """
        Predict using the Gaussian process regression model.

        In addition to the mean of the predictive distribution, optionally also
        returns its standard deviation (`return_std=True`).

        Arguments:
        ----------
        X : ndarray of shape (n_samples, n_features)
            Query points where the GP is evaluated.
        return_std : bool, default=False
            If True, the standard-deviation of the predictive distribution at
            the query points is returned along with the mean.

        Returns (depending on the case):
        --------------------------------
        y_mean : ndarray of shape (n_samples, n_targets)
            Mean of predictive distribution a query points.
        y_std : ndarray of shape (n_samples, n_targets), optional
            Standard deviation of predictive distribution at query points.
            Only returned when `return_std` is True.
        """
        # TODO Q2.2
        # Implement the predictive distribution of the Gaussian Process Regression
        # by using the Algorithm (1) from the assignment sheet.

        # FIXME
#         print("2.2 ============================")
#         print("X_train:", self._X_train, self._X_train.shape)
#         print("X:", X, X.shape)
#         print("Y:", self._y_train, self._y_train.shape)
        
        Ky = self.kernel(X)
        Ky = Ky + self.noise_level * np.identity(len(Ky))
#         print("Ky:", Ky, Ky.shape)

        L = cholesky(Ky).T
#         print("L:", L, L.shape)
        alpha = np.linalg.solve(L.T,  np.linalg.solve(L, self._y_train))
#         print("alpha:", alpha, alpha.shape)

        K_T = self.kernel(X, self._X_train)
#         print("K*T:", K_T, K_T.shape)
        K_ = self.kernel(self._X_train, X)
#         print("K*:", K_, K_.shape)

        F_ = K_T @ alpha
#         print("F* (mu):", F_, F_.shape)
#         F_ = K_T @ np.linalg.inv(Ky) @ self._y_train
#         print("F* (mu):", F_, F_.shape)

        K__ = self.kernel(X)
#         print("K**:", K__, K__.shape)
        
        v = np.linalg.solve(L, K_)
#         print("v2:", v, v.shape)

        v = np.linalg.solve(L, K_)
#         print("v:", v, v.shape)
        V = K__ - v.T @ v
#         print("V (sigma):", V, V.shape)
        
        Vf = []
        for i in range(len(V)):
            Vf.append(V[i, i] ** 0.5)
        Vf = np.array(Vf)
        if return_std:
            return F_, Vf
        return F_
        
        
        raise NotImplementedError

    def fit_and_predict(self, X_train: np.ndarray, y_train: np.ndarray, X_test: np.ndarray,
                        return_std: bool = False, optimise_fit: bool = False
                        ) -> Union[
        Tuple[dict, Union[np.ndarray, Tuple[np.ndarray, np.ndarray]]],
        Union[np.ndarray, Tuple[np.ndarray, np.ndarray]]
    ]:
        """
        Predict and/or fit using the Gaussian process regression model.

        Based on the value of optimise_fit, we either perform predictions with or without fitting the GP first.

        Arguments:
        ----------
        X_train : ndarray of shape (n_samples, n_features)
            Feature vectors or other representations of training data.
        y_train : ndarray of shape (n_samples, n_targets)
            Target values.
        X_test : ndarray of shape (n_samples, n_features)
            Query points where the GP is evaluated.
        return_std : bool, default=False
            If True, the standard-deviation of the predictive distribution at
            the query points is returned along with the mean.
        optimise_fit : bool, default=False
            If True, we first perform fitting and then we continue with the 
            prediction. Otherwise, perform only inference.

        Returns (depending on the case):
        --------------------------------
        kernel_params: the kernel (hyper)parameters, optional. Only for `optimise_fit=True` case; 
            HINT: use `get_params()` fuction from kernel object. 
        y_mean : ndarray of shape (n_samples, n_targets)
            Mean of predictive distribution a query points.
        y_std : ndarray of shape (n_samples, n_targets), optional
            Standard deviation of predictive distribution at query points.
            Only returned when `return_std` is True.

        """
        # TODO Q2.6a
        # Implement a fit and predict or only predict scenarios. The course of action
        # should be chosen based on the variable `optimise_fit`.
        if optimise_fit:
            # FIXME
            
            self.fit(X_train, y_train)
            y_mean, y_std = self.predict(X_test, True)
            return self.kernel.theta, y_mean, y_std
            raise NotImplementedError
        else:
            self._kernel = copy.deepcopy(self.kernel)

            self._X_train = X_train
            self._y_train = y_train

            # FIXME
            y_mean, y_std = self.predict(X_test, True)
            return self.kernel.theta, y_mean, y_std
            
            raise NotImplementedError

    def log_marginal_likelihood(self, theta: np.ndarray) -> float:
        """
        Return log-marginal likelihood of theta for training data.

        Arguments:
        ----------
        theta : ndarray of shape (n_kernel_params,)
            Kernel hyperparameters for which the log-marginal likelihood is
            evaluated.

        Returns:
        --------
        log_likelihood : float
            Log-marginal likelihood of theta for training data.
        """
        # TODO Q2.2
        # Compute the log marginal likelihood by using the Algorithm (1)
        # from the assignment sheet.

        kernel = self._kernel
        kernel.theta = theta

        # FIXME
        
        print("theta :", theta, theta.shape)
        print("X_train:", self._X_train, self._X_train.shape)
        print("y_train:", self._y_train, self._y_train.shape)
        
        n = self._X_train.shape[0]
        
        Ky = self.kernel(self._X_train)
        Ky = Ky + self.noise_level * np.identity(len(Ky))
        print("Ky:", Ky, Ky.shape)
        L = cholesky(Ky).T
        print("L:", L, L.shape)
        alpha = np.linalg.solve(L, self._y_train)
        print("alpha':", alpha, alpha.shape)
        alpha = np.linalg.solve(L.T, alpha)
        print("alpha:", alpha, alpha.shape)
        L_ii = 0
        for i in range(len(L)):
            L_ii += np.log(L[i, i])
            
        print("L_ii:", L_ii)
        
        res = -0.5 * self._y_train.T @ alpha - L_ii - (n / 2) * np.log(2 * np.pi)      # pi??
        res = res[0,0]
        
        print("res:", res)
        
        return res
        
        raise NotImplementedError
