import numpy as np
from scipy.stats import norm

# Functional Structure


def probability_improvement(X: np.ndarray, X_sample: np.ndarray,
                            gpr: object, xi: float = 0.01) -> np.ndarray:
    """
    Probability improvement acquisition function.

    Computes the PI at points X based on existing samples X_sample using
    a Gaussian process surrogate model

    Arguments:
    ----------
        X: ndarray of shape (m, d)
            The point for which the expected improvement needs to be computed.

        X_sample: ndarray of shape (n, d)
            Sample locations

        gpr: GPRegressor object.
            Gaussian process trained on previously evaluated hyperparameters.

        xi: float. Default 0.01
            Exploitation-exploration trade-off parameter.

    Returns:
    --------
        PI: ndarray of shape (m,)
    """
    # TODO Q2.4
    # Implement the probability of improvement acquisition function

    # FIXME
    
    print(">>> X:", X, X.shape)
    print(">>> X_sample:", X_sample, X_sample.shape)
    gpr.fit(X, X_sample.T)
    y_mean, y_std = gpr.predict(X, True)
    print(">>> y_mean:", y_mean, y_mean.shape)
    print(">>> y_std:", y_std, y_std.shape)

    if y_std.all() == 0:
        return 0
#     print(">>> gpr.kernel(X, X_sample):", gpr.kernel(X, X_sample))
#     Z = y_mean - gpr.kernel(X, X_sample)
    fx_ = norm(y_mean, y_std).pdf(X)
    print(">>> fx:", fx_, fx_.shape)
    Z = y_mean - fx_ - xi
    Z = Z / y_std
    print(">>> Z:", Z, Z.shape)
    
    print(">>> PI res:", norm(y_mean, y_std).cdf(Z).T)
    
    return norm(y_mean, y_std).cdf(Z)[0].T
    
    
    raise NotImplementedError


def expected_improvement(X: np.ndarray, X_sample: np.ndarray,
                         gpr: object, xi: float = 0.01) -> np.ndarray:
    """
    Expected improvement acquisition function.

    Computes the EI at points X based on existing samples X_sample using
    a Gaussian process surrogate model

    Arguments:
    ----------
        X: ndarray of shape (m, d)
            The point for which the expected improvement needs to be computed.

        X_sample: ndarray of shape (n, d)
            Sample locations

        gpr: GPRegressor object.
            Gaussian process trained on previously evaluated hyperparameters.

        xi: float. Default 0.01
            Exploitation-exploration trade-off parameter.

    Returns:
    --------
        EI : ndarray of shape (m,)
    """

    # TODO Q2.4
    # Implement the expected improvement acquisition function

    # FIXME

    gpr.fit(X, X_sample.T)
    y_mean, y_std = gpr.predict(X, True)

    if y_std.all() == 0:
        return 0

    N = norm(y_mean, y_std)

    fx_ = N.pdf(X)

    Z = y_mean - fx_ - xi
    Z = Z / y_std
    print(">>> Z:", Z, Z.shape)
    return Z*y_std*N.cdf(Z)[0] + y_std * N.pdf(Z)
        

    raise NotImplementedError
