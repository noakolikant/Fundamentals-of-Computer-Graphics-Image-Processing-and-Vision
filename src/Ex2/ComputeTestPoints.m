function [ pnts_gt, pnts_computed ] = ComputeTestPoints(H_gt,H_computed)
%ComputeTestPoints Computes the ground truth points and the points 
% generated from the H_computed.
    NUMBER_OF_RANDOM_POINTS = 100;
    pnts_gt = zeros(NUMBER_OF_RANDOM_POINTS, 3);
    pnts_computed = zeros(NUMBER_OF_RANDOM_POINTS, 3);
    random_points = randi([1, 512], NUMBER_OF_RANDOM_POINTS, 2);
    for i=1:size(pnts_gt, 1)
        pnts_gt(i, :) = H_gt * [random_points(i,:), 1]';
        pnts_gt(i, :) = pnts_gt(i, :) / pnts_gt(i, 3);
        pnts_computed(i, :) = H_computed * [random_points(i,:), 1]';
        pnts_computed(i, :) = pnts_computed(i, :) / pnts_computed(i, 3);       
    end
end

