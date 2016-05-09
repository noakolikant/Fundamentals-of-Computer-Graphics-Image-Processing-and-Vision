function [ error ] = ComputeError(pnts_gt, pnts_computed)
%ComputeError Computes the L1 distance between the points x' (the ground
% truth points) and Hx (the computed points from the homography) 
    error = 0;
    for i = 1:min(size(pnts_gt, 1), size(pnts_computed))
        % Take only the Hom. parts
        error = error + ((pnts_gt(i, 1) - pnts_computed(i, 1)).^2 + ...
            (pnts_gt(i, 2) - pnts_computed(i, 2)).^2);
    end


end

