import numpy as np

################################################################
##### BLR Question Code
################################################################

def single_EM_iter_blr(features, targets, alpha_i, beta_i):
    # Given the old alpha_i and beta_i, computes expectation of latent variable w: M_n and S_n,
    # and using that computes the new alpha and beta values.
    # Should return M_n, S_n, new_alpha, new_beta in that order, with return shapes (M,1), (M,M), None, None
    ### CODE HERE ###
#     raise NotImplementedError
    # features: phi
    # targets: t
    # alpha_i: int
    # beta_i: int
#     print("features:", features.shape)
    N, M = features.shape
    mn = np.zeros((M, 1))
    sn_ = alpha_i * np.identity(M) + beta_i * features.T @ features
    sn = np.linalg.inv(sn_)
    mn = beta_i * sn @ features.T @ targets
    new_alpha = M / (mn.T @ mn + np.trace(sn))
    new_alpha = new_alpha[0][0]
    new_beta = N / (((targets - features @ mn) ** 2).sum() + np.trace(features.T @ features @ sn))
#     print("M, N:" , M, N)
#     print("mn:", mn.shape, ", sn:", sn.shape)
#     print("new_alpha:", new_alpha, ", new_beta:", new_beta)
    return mn, sn, new_alpha, new_beta # (M,1), (M,M), None, None