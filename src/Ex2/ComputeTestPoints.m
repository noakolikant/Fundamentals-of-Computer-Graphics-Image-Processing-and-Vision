function [ pnts_gt, pnts_computed ] = ComputeTestPoints(H_gt,H_computed)
%ComputeTestPoints Computes the ground truth points and the points 
% generated from the H_computed.
    pnts_gt = zeros(512, 512, 3);
    pnts_computed = zeros(512,512, 3);
    for i=1:size(pnts_gt, 1)
        for j=1:size(pnts_gt, 2)
            pnts_gt(i, j, :) = H_gt * [i, j, 1]';
            pnts_gt(i, j, 1:3) = pnts_gt(i, j, 1:3) / pnts_gt(i, j, 3);
            pnts_computed(i, j, :) = H_computed * [i, j, 1]';
            pnts_computed(i, j, 1:3) = pnts_computed(i, j, 1:3) / ...
                pnts_computed(i, j, 3);
        end
    end


end

