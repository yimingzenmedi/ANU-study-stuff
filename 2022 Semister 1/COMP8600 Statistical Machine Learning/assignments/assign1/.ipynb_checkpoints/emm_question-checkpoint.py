import numpy as np
from functools import lru_cache
from scipy.integrate import quad

################################################################
##### Helper functions (DO NOT CHANGE)
################################################################

# @lru_cache  # Makes things go fast
def normalise_expontial_family(sufstat, eta):
    unnorm_prob = lambda z: np.exp(sufstat(z) @ np.array(eta))
    Z, err = quad(unnorm_prob, -np.inf, np.inf)
    return float(Z)

def exponential_family_pdf(x, sufstat, eta):
    # Input Shapes: (1,), None, (M,)
    # sufstat designates the sufficient statistic map for the exponential
    # family, taking values in (1,) to (M,).
    unnorm_prob = lambda z: np.exp(sufstat(z) @ eta)
    eta = eta.squeeze()
    Z = normalise_expontial_family(sufstat, tuple(eta))
    prob = unnorm_prob(x) / Z # Here Z = exp(-psi(eta))
    
    return prob

################################################################
##### EMM Question Code
################################################################

def weighted_probs(data, pi, eta, sufstat, N, K):
    # Input Shapes: (N,), (K,1), (K,m), None, None
    # Should implement pi_k * q(x_n|eta_k) for each n, k, and thus return shape
    # should be (N,K). You should use exponential_family_pdf as defined above.
    # Note: sufstat(x) = u(x).
    # Works for scalars ((1,) -> (2,)); and 1D arrays ((N,) -> (N, 2)).
    ### CODE HERE ###
    
#     raise NotImplementedError
    probs = np.zeros((N, K))
    for n in range(N):
        for k in range(K):
            x = data[n]
            q = exponential_family_pdf(x, sufstat, eta[k])
            probs[n, k] = pi[k] * q
#     print("probs:", probs.shape)
    return probs # (N, K)

import time

def e_step_EMM(data, pi, eta, sufstat, N, K):
    # Input Shapes: (N,), (K,1), (K,m), None, None
    # Should implement gamma_nk for each n, k; and thus return shape should be (N,K).
    # Note: sufstat(x) = u(x).
    # This works for scalars ((1,) -> (2,)); and 1D arrays ((N,) -> (N, 2)).
    # It should use weighted_probs.
    ### CODE HERE ###
#     raise NotImplementedError
    start = time.time()
    probs = weighted_probs(data, pi, eta, sufstat, N, K)
    
    b_ = probs.sum(axis=1)
    gamma = probs / b_[:, None]

#     gamma = np.zeros((N, K))
#     s = 0
#     for n in range(N):
#         for k in range(K):
#             a = probs[n, k]
#             b = probs.sum(axis=1)[n]
#             gamma[n, k] = a / b
#     print("gamma:", gamma.shape)

#     print(gamma.shape, gamma)
#     print(gamma_.shape, gamma_)
    end = time.time()
    print(end-start)
    return gamma # (N, K)

def m_step_EMM(data, gamma, sufstat, exp_to_nat, N, K):
    # Input Shapes: (N,D), (N,K), None, None, None
    # Should implement updates for pi, Eta, and return them in that order.
    # exp_to_nat is a function which converts the expectation parameter to
    # natural parameter. This only works dimensions (2,) -> (2,).
    # Note: sufstat(x) = u(x).
    # This works for scalars (1,) -> (2,); and 1D arrays (N,) -> (N, 2).
    # Return shapes should be (K,1), (K,m).
    ### CODE HERE ###
    # raise NotImplementedError
#     print("N:", N)
#     print("gamma:", gamma.shape)
    N_k = gamma.sum(axis=0)
#     print("N_k:", N_k.shape)
    pi_new = N_k / N
    pi_new = pi_new.reshape((K, 1))
    u = np.array([sufstat(x) for x in data])
#     print("u:", u.shape)
    lam = u.T @ gamma / N_k
    eta_new = exp_to_nat(lam).T
    
#     print("pi_new:", pi_new.shape)
#     print("eta_new:", eta_new.shape)
    
    return pi_new, eta_new # (K,1), (K,m)